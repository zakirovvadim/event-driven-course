package ru.vadim.orderservice.query;

import lombok.Value;

@Value
public class FindOrderQuery {

    private final String orderId;

    public FindOrderQuery(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
