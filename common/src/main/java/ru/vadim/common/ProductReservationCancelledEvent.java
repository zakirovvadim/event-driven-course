package ru.vadim.common;

public class ProductReservationCancelledEvent {

    private final String productId;
    private final int quantity;
    private final String orderId;
    private final String userId;
    private final String reason;

    public ProductReservationCancelledEvent(String productId, int quantity, String orderId, String userId, String reason) {
        this.productId = productId;
        this.quantity = quantity;
        this.orderId = orderId;
        this.userId = userId;
        this.reason = reason;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getReason() {
        return reason;
    }
}
