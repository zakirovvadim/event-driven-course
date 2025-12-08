package ru.vadim.orderservice.core.events;

import ru.vadim.orderservice.core.model.OrderStatus;

public class OrderApprovedEvent {
    private final String orderId;
    private final OrderStatus orderStatus = OrderStatus.APPROVED;

    public OrderApprovedEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
