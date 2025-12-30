package com.ecommerce.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Slf4j
@Service
public class OrderEventConsumer {
//    @RabbitListener(queues = "${rabbitmq.queue.name}")
//    public void handleOrderEvent(OrderCreatedEvent orderEvent) {
//        System.out.println("Received Order Event: " + orderEvent);
//
//        long orderId = orderEvent.getOrderId();
//        OrderStatus orderStatus = orderEvent.getStatus();
//
//        System.out.println("Order ID: " + orderId);
//        System.out.println("Order Status: " + orderStatus);
//
//        //More Applications of Notification Service -
//        // Update Database
//        // Send Notification
//        // Send Emails
//        // Generate Invoices
//        // Send Seller Notifications
//    }

    @Bean
    public Consumer<OrderCreatedEvent> orderCreated() {
        return event -> {
            log.info("Received order created event for order: {}", event.getOrderId());
            log.info("Received order created event for user id: {}", event.getUserId());
        };
    }
}
