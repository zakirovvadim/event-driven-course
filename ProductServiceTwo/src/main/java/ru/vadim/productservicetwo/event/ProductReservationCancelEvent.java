package ru.vadim.productservicetwo.event;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ProductReservationCancelEvent {
    @TargetAggregateIdentifier
    private final String productId;
    private final int quantity;
    private final String orderId;
    private final String userId;
    private final String reason;

    public ProductReservationCancelEvent(String productId, int quantity, String orderId, String userId, String reason) {
        this.productId = productId;
        this.quantity = quantity;
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
    }

    public String getProductId() {
        return this.productId;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public String getOrderId() {
        return this.orderId;
    }

    public String getUserId() {
        return this.userId;
    }

    public String getReason() {
        return this.reason;
    }
}
