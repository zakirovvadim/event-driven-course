package ru.vadim.orderservice.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class ApprovedOrderCommand {
    @TargetAggregateIdentifier
    private final String orderId;

    public ApprovedOrderCommand(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}
