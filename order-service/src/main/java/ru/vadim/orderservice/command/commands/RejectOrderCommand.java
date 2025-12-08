package ru.vadim.orderservice.command.commands;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class RejectOrderCommand {

    @TargetAggregateIdentifier
    private final String orderId;
    private final String reason;

    public RejectOrderCommand(String orderId, String reason) {
        this.orderId = orderId;
        this.reason = reason;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getReason() {
        return reason;
    }
}
