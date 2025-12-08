/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vadim.orderservice.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import ru.vadim.orderservice.core.data.OrderEntity;
import ru.vadim.orderservice.core.data.OrdersRepository;
import ru.vadim.orderservice.core.events.OrderApprovedEvent;
import ru.vadim.orderservice.core.events.OrderCreatedEvent;
import ru.vadim.orderservice.core.events.OrderRejectEvent;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
    
    private final OrdersRepository ordersRepository;
    
    public OrderEventsHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event){
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);
 
        this.ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderApprovedEvent orderApprovedEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderApprovedEvent.getOrderId());
        if (orderEntity == null) {
            // TO DO smth
            return;
        }
        orderEntity.setOrderStatus(orderApprovedEvent.getOrderStatus());
        this.ordersRepository.save(orderEntity);
    }

    @EventHandler
    public void on(OrderRejectEvent orderRejectEvent) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(orderRejectEvent.getOrderId());
        orderEntity.setOrderStatus(orderRejectEvent.getOrderStatus());
        this.ordersRepository.save(orderEntity);
    }
}
