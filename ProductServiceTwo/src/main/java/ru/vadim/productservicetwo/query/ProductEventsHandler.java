package ru.vadim.productservicetwo.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.interceptors.ExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.vadim.common.ProductReservedEvent;
import ru.vadim.productservicetwo.core.data.ProductEntity;
import ru.vadim.productservicetwo.event.ProductCreatedEvent;
import ru.vadim.productservicetwo.event.ProductReservationCancelEvent;
import ru.vadim.productservicetwo.repository.ProductRepository;

@Component
@ProcessingGroup("product-group")
public class ProductEventsHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductEventsHandler.class);

    private final ProductRepository productRepository;

    public ProductEventsHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // ловит только в том же классе, те здесь
    @ExceptionHandler(resultType = IllegalArgumentException.class)
    public void handle(IllegalArgumentException exception) {
    }

    @ExceptionHandler(resultType = Exception.class)
    public void handle(Exception exception) throws Exception {
        throw exception;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) throws Exception {
        ProductEntity productEntity = new ProductEntity();
        BeanUtils.copyProperties(event, productEntity);

        productRepository.save(productEntity);

//        if (true) throw new Exception("Forcing exception in the Event handler class");
        // если будет искючение в хендлере оно пробросится выше в ProductServiceEventsErrorHandler и транзакция откатится
    }

    @EventHandler // ловлю ивент на обновление количества продуктов, при резервировании из агрегата
    public void on(ProductReservedEvent productReservedEvent) {
        ProductEntity productEntity = productRepository.findByProductId(productReservedEvent.getProductId());

        log.debug("ProductReservedEvent: Current product quantity " + productEntity.getQuantity());

        productEntity.setQuantity(productEntity.getQuantity() - productReservedEvent.getQuantity());
        productRepository.save(productEntity);

        log.debug("ProductReservedEvent: New product quantity " + productEntity.getQuantity() );

        log.info("ProductReservedEvent is called for productId: " + productReservedEvent.getProductId() + " and orderId: " +
                productReservedEvent.getOrderId());
    }

    @EventHandler // также ловим тут чтобы изменить данные о количестве в бд
    public void on(ProductReservationCancelEvent productReservationCancelEvent) {
        ProductEntity currentStoredProduct = productRepository.findByProductId(productReservationCancelEvent.getProductId());

        log.debug("ProductReservedEvent: Current product quantity " + currentStoredProduct.getQuantity());
        int newQuantity = currentStoredProduct.getQuantity() + productReservationCancelEvent.getQuantity();
        currentStoredProduct.setQuantity(newQuantity);
        productRepository.save(currentStoredProduct);

        log.debug("ProductReservedEvent: New product quantity " + currentStoredProduct.getQuantity());
    }
}
