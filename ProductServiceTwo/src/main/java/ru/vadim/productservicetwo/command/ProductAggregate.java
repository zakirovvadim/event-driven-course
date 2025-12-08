package ru.vadim.productservicetwo.command;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import ru.vadim.common.ProductReservedEvent;

import ru.vadim.common.ReserveProductCommand;
import ru.vadim.common.command.CancelProductReservationCommand;
import ru.vadim.productservicetwo.event.ProductCreatedEvent;
import ru.vadim.productservicetwo.event.ProductReservationCancelEvent;

import java.math.BigDecimal;

@Aggregate
public class ProductAggregate {

    @AggregateIdentifier
    private String productId;
    private String title;
    private BigDecimal price;
    private Integer quantity;

    public ProductAggregate() {

    }

    @CommandHandler
    public ProductAggregate(CreateProductCommand createProductCommand) {
        if (createProductCommand.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price cannot be less or equal than zero");
        }

        if (createProductCommand.getTitle() == null || createProductCommand.getTitle().isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }

        ProductCreatedEvent productCreatedEvent = new ProductCreatedEvent();
        BeanUtils.copyProperties(createProductCommand, productCreatedEvent);
        AggregateLifecycle.apply(productCreatedEvent);
    }

    @CommandHandler // ловим команду на резервирование, если на складе достаточно средств - опубликуем событие о резервировании
    public void handle(ReserveProductCommand reserverProductCommand) {
        if (quantity < reserverProductCommand.getQuantity()) {
            throw new IllegalArgumentException("Insufficient number of items in stock");
        }

        ProductReservedEvent productReservedEvent = new ProductReservedEvent(
                reserverProductCommand.getProductId(),
                reserverProductCommand.getQuantity(),
                reserverProductCommand.getOrderId(),
                reserverProductCommand.getUserId()
        );
        AggregateLifecycle.apply(productReservedEvent); // goto  public void on(ProductReservedEvent productReservedEvent) {
    }


    @CommandHandler
    public void handle(CancelProductReservationCommand cancelProductReservationCommand) {
        ProductReservationCancelEvent productReservationCancelEvent = new ProductReservationCancelEvent(
                cancelProductReservationCommand.getProductId(),
                cancelProductReservationCommand.getQuantity(),
                cancelProductReservationCommand.getOrderId(),
                cancelProductReservationCommand.getUserId(),
                cancelProductReservationCommand.getReason()
        );

        AggregateLifecycle.apply(productReservationCancelEvent);
    }

    @EventSourcingHandler
    public void on(ProductReservationCancelEvent productReservationCancelEvent) {
        this.quantity += productReservationCancelEvent.getQuantity(); // возвращаем ранее зарезервированные товары
    }

    @EventSourcingHandler
    public void on(ProductCreatedEvent productCreatedEvent) {
        this.productId = productCreatedEvent.getProductId();
        this.price = productCreatedEvent.getPrice();
        this.title = productCreatedEvent.getTitle();
        this.quantity = productCreatedEvent.getQuantity();
    }

    @EventSourcingHandler
    public void on(ProductReservedEvent productReservedEvent) {
        //И теперь внутри этого обработчика событий нам нужно обновить агрегированные свойства
        this.quantity -= productReservedEvent.getQuantity(); //Когда это событие применяется Axon Framework сохранит резерв продукта объект события в хранилище событий.
    }
}
