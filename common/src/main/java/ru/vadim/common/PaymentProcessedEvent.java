package ru.vadim.common;

public class PaymentProcessedEvent {
    private final String orderId;
    private final String paymentId;

    public PaymentProcessedEvent(String orderId, String paymentId) {
        this.orderId = orderId;
        this.paymentId = paymentId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPaymentId() {
        return paymentId;
    }
}