/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.vadim.orderservice.command.rest;

import java.util.UUID;

import jakarta.validation.Valid;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.SubscriptionQueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.vadim.orderservice.command.commands.CreateOrderCommand;
import ru.vadim.orderservice.core.model.OrderStatus;
import ru.vadim.orderservice.core.model.OrderSummary;
import ru.vadim.orderservice.query.FindOrderQuery;

@RestController
@RequestMapping("/orders")
public class OrdersCommandController {

    private final CommandGateway commandGateway;
    private final QueryGateway queryGateway;

    @Autowired
    public OrdersCommandController(CommandGateway commandGateway, QueryGateway queryGateway) {
        this.commandGateway = commandGateway;
        this.queryGateway = queryGateway;
    }

    @PostMapping
    public OrderSummary createOrder(@Valid @RequestBody OrderCreateRest order) {

        String userId = "27b95829-4f3f-4ddf-8983-151ba010e35b";

        String orderId = UUID.randomUUID().toString();

        CreateOrderCommand createOrderCommand = new CreateOrderCommand(
                orderId,
                userId,
                order.getProductId(),
                order.getQuantity(),
                order.getAddressId(),
                OrderStatus.CREATED);

        /*
        Сценарий такой:
REST-контроллер:
открывает subscription query;
шлёт команду на создание заказа;
Командный хэндлер → события → Saga → проекция обновляет запись OrderSummary и через QueryUpdateEmitter посылает апдейт всем подпискам FindOrderQuery(orderId);
Этот апдейт попадает в queryResult.updates() → blockFirst() возвращает OrderSummary;
Контроллер возвращает его клиенту.
         */
        try (SubscriptionQueryResult<OrderSummary, OrderSummary> queryResult = queryGateway.subscriptionQuery(
                new FindOrderQuery(orderId),
                ResponseTypes.instanceOf(OrderSummary.class),
                ResponseTypes.instanceOf(OrderSummary.class))) {

            commandGateway.sendAndWait(createOrderCommand);
            return queryResult.updates().blockFirst();
        }

    }

}
