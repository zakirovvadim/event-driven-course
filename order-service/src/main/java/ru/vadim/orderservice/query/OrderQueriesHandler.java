package ru.vadim.orderservice.query;

import org.axonframework.queryhandling.QueryHandler;
import org.springframework.stereotype.Component;
import ru.vadim.orderservice.core.data.OrderEntity;
import ru.vadim.orderservice.core.data.OrdersRepository;
import ru.vadim.orderservice.core.model.OrderSummary;

@Component
public class OrderQueriesHandler {

    private final OrdersRepository ordersRepository;

    public OrderQueriesHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @QueryHandler
    public OrderSummary findOrder(FindOrderQuery findOrderQuery) {
        OrderEntity orderEntity = ordersRepository.findByOrderId(findOrderQuery.getOrderId());
        return new OrderSummary(orderEntity.getOrderId(), orderEntity.getOrderStatus(), "");
    }
}
