package ru.vadim.orderservice.saga;

import org.axonframework.commandhandling.CommandCallback;
import org.axonframework.commandhandling.CommandMessage;
import org.axonframework.commandhandling.CommandResultMessage;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vadim.common.PaymentProcessedEvent;
import ru.vadim.common.ProductReservationCancelledEvent;
import ru.vadim.common.ProductReservedEvent;
import ru.vadim.common.ReserveProductCommand;
import ru.vadim.common.command.CancelProductReservationCommand;
import ru.vadim.common.command.ProcessPaymentCommand;
import ru.vadim.common.model.User;
import ru.vadim.common.query.FetchUserPaymentDetailsQuery;
import ru.vadim.orderservice.command.commands.ApprovedOrderCommand;
import ru.vadim.orderservice.command.commands.RejectOrderCommand;
import ru.vadim.orderservice.core.events.OrderApprovedEvent;
import ru.vadim.orderservice.core.events.OrderCreatedEvent;
import ru.vadim.orderservice.core.events.OrderRejectEvent;

import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Saga
public class OrderSaga {

    @Autowired
    private transient CommandGateway commandGateway;

    @Autowired
    private transient QueryGateway queryGateway;

    private static final Logger log = LoggerFactory.getLogger(OrderSaga.class);

    @StartSaga
    @SagaEventHandler(associationProperty = "orderId") // начало саги, отправялем команду на резервирование продукта
    public void handle(OrderCreatedEvent orderCreatedEvent) {
        ReserveProductCommand reserveProductCommand = new ReserveProductCommand(
                orderCreatedEvent.getProductId(),
                orderCreatedEvent.getQuantity(),
                orderCreatedEvent.getOrderId(),
                orderCreatedEvent.getUserId()
        );
        log.info("OrderCreatedEvent handled for orderId: " + reserveProductCommand.getOrderId() + " and productId: " +
                reserveProductCommand.getProductId());
        commandGateway.send(reserveProductCommand, new CommandCallback<ReserveProductCommand, Object>() {
            @Override
            public void onResult(@Nonnull CommandMessage<? extends ReserveProductCommand> commandMessage, @Nonnull CommandResultMessage<?> commandResultMessage) {
                if (commandResultMessage.isExceptional()) {
                    // Start a compensation transaction
                }
            }
        });
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservedEvent productReservedEvent) {
        // Process user payment
        log.info("ProductReservedEvent is called for productId: " + productReservedEvent.getProductId() + " and orderId: " +
                productReservedEvent.getOrderId());
        FetchUserPaymentDetailsQuery fetchUserPaymentDetailsQuery = new FetchUserPaymentDetailsQuery(productReservedEvent.getUserId());
        User userPaymentDetails = null;
        try {
            userPaymentDetails = queryGateway.query(fetchUserPaymentDetailsQuery, ResponseTypes.instanceOf(User.class)).join();
        } catch (Exception ex) {
            log.info(ex.getMessage());
            cancelProductReservation(productReservedEvent, ex.getMessage());
            return;
        }

        if (userPaymentDetails == null) {
            cancelProductReservation(productReservedEvent, "Could not fetch user payment details");
            return;
        }
        log.info("Successfully fetched uer payment details for user " + userPaymentDetails.getFirstName());

        ProcessPaymentCommand processPaymentCommand = new ProcessPaymentCommand(
                UUID.randomUUID().toString(),
                productReservedEvent.getOrderId(),
                userPaymentDetails.getPaymentDetails()
        );
        String result= null;
        try {
           result = commandGateway.sendAndWait(processPaymentCommand, 10, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.info(e.getMessage());
            cancelProductReservation(productReservedEvent, e.getMessage());
            return;
        }
        if (result == null) {
            log.info("The ProcessPaymentCommand resulted in NULL. Initialing a compensating transaction");
            cancelProductReservation(productReservedEvent, "Could not process user payment with provided payment details");
        }
    }

    private void cancelProductReservation(ProductReservedEvent productReservedEvent, String reason) {
        CancelProductReservationCommand cancelProductReservationCommand = new CancelProductReservationCommand(
                productReservedEvent.getProductId(),
                productReservedEvent.getQuantity(),
                productReservedEvent.getOrderId(),
                productReservedEvent.getUserId(),
                reason
        );
        commandGateway.send(cancelProductReservationCommand)
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
        ApprovedOrderCommand approvedOrderCommand = new ApprovedOrderCommand(paymentProcessedEvent.getOrderId());
        commandGateway.send(approvedOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderApprovedEvent orderApprovedEvent) {
        log.info("Order is approved. Order saga is complete for orderId: {}", orderApprovedEvent.getOrderId());
//        SagaLifecycle.end(); // больше не будет обрабатывать новые события
    }

    @SagaEventHandler(associationProperty = "orderId")
    public void handle(ProductReservationCancelledEvent productReservationCancelledEvent) {
        RejectOrderCommand rejectOrderCommand = new RejectOrderCommand(productReservationCancelledEvent.getOrderId(), productReservationCancelledEvent.getReason());
        commandGateway.send(rejectOrderCommand);
    }

    @EndSaga
    @SagaEventHandler(associationProperty = "orderId")
    public void handle(OrderRejectEvent orderRejectEvent) {
        log.info("Successfully  rejected order with id " + orderRejectEvent.getOrderId());
    }
}
