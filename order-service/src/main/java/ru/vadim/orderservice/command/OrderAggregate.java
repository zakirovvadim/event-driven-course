/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vadim.orderservice.command;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;
import ru.vadim.orderservice.command.commands.ApprovedOrderCommand;
import ru.vadim.orderservice.command.commands.CreateOrderCommand;
import ru.vadim.orderservice.command.commands.RejectOrderCommand;
import ru.vadim.orderservice.core.events.OrderApprovedEvent;
import ru.vadim.orderservice.core.events.OrderCreatedEvent;
import ru.vadim.orderservice.core.events.OrderRejectEvent;
import ru.vadim.orderservice.core.model.OrderStatus;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private String orderId;
    private String productId;
    private String userId;
    private int quantity;
    private String addressId;
    private OrderStatus orderStatus;
    
    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand createOrderCommand) {
        OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent();
        BeanUtils.copyProperties(createOrderCommand, orderCreatedEvent);
        
        AggregateLifecycle.apply(orderCreatedEvent);
    }

    @EventSourcingHandler
    public void on(OrderCreatedEvent orderCreatedEvent) throws Exception {
        this.orderId = orderCreatedEvent.getOrderId();
        this.productId = orderCreatedEvent.getProductId();
        this.userId = orderCreatedEvent.getUserId();
        this.addressId = orderCreatedEvent.getAddressId();
        this.quantity = orderCreatedEvent.getQuantity();
        this.orderStatus = orderCreatedEvent.getOrderStatus();
    }

    @CommandHandler
    public void handle(ApprovedOrderCommand approvedOrderCommand) {
        // Create and publish the OrderApprvedEvent
        OrderApprovedEvent orderApprovedEvent = new OrderApprovedEvent(approvedOrderCommand.getOrderId());
        AggregateLifecycle.apply(orderApprovedEvent);
    }

    @CommandHandler
    public void handle(RejectOrderCommand rejectOrderCommand) {
        OrderRejectEvent orderRejectEvent = new OrderRejectEvent(rejectOrderCommand.getOrderId(), rejectOrderCommand.getReason());
        AggregateLifecycle.apply(orderRejectEvent);
    }

    @EventSourcingHandler
    protected void on(OrderApprovedEvent orderApprovedEvent) {
        this.orderStatus = orderApprovedEvent.getOrderStatus();
    }

    @EventSourcingHandler
    protected void on(OrderRejectEvent orderRejectEvent) {
        this.orderStatus = orderRejectEvent.getOrderStatus();
    }

}
