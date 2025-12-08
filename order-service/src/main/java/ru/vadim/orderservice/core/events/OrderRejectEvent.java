package ru.vadim.orderservice.core.events;

import ru.vadim.orderservice.core.model.OrderStatus;

public class OrderRejectEvent {
    private final String orderId;
    private final String reason;
    private final OrderStatus orderStatus = OrderStatus.REJECTED;

    public OrderRejectEvent(String orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getReason() {
        return reason;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
