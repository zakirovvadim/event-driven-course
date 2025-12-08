/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vadim.orderservice.command.commands;

import lombok.Builder;
import lombok.Data;
import org.axonframework.modelling.command.TargetAggregateIdentifier;
import ru.vadim.orderservice.core.model.OrderStatus;

@Builder
@Data
public class CreateOrderCommand {
        
    @TargetAggregateIdentifier
    public final String orderId;
    
    private final String userId;
    private final String productId;
    private final int quantity;
    private final String addressId; 
    private final OrderStatus orderStatus;

    public CreateOrderCommand(String orderId, String userId, String productId, int quantity, String addressId, OrderStatus orderStatus) {
        this.orderId = orderId;
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
        this.addressId = addressId;
        this.orderStatus = orderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public String getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getAddressId() {
        return addressId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }
}
