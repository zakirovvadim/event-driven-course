package ru.vadim.productservicetwo.command;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;
import ru.vadim.productservicetwo.core.data.ProductLookupEntity;
import ru.vadim.productservicetwo.core.data.ProductLookupRepository;
import ru.vadim.productservicetwo.event.ProductCreatedEvent;

@Component
@ProcessingGroup("product-group") // обрабатывает только один раз одно и тоже событие
public class ProductLookupEventsHandler {

    private final ProductLookupRepository productLookupRepository;

    public ProductLookupEventsHandler(ProductLookupRepository productLookupRepository) {
        this.productLookupRepository = productLookupRepository;
    }

    @EventHandler
    public void on(ProductCreatedEvent event) {
        ProductLookupEntity productLookupEntity = new ProductLookupEntity(event.getProductId(), event.getTitle());
        productLookupRepository.save(productLookupEntity);
    }
}
