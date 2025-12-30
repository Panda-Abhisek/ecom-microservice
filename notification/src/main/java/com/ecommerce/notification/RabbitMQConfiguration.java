package com.ecommerce.notification;

//@Configuration
//public class RabbitMQConfiguration {
//    @Value("${rabbitmq.queue.name}")
//    private String queueName;
//
//    @Value("${rabbitmq.exchange.name}")
//    private String exchangeName;
//
//    @Value("${rabbitmq.routing.key}")
//    private String routingKey;
//
//    @Bean
//    public Queue queue() {
//        return QueueBuilder.durable(queueName).build();
//    }
//
//    @Bean
//    public TopicExchange exchange() {
//        return ExchangeBuilder.topicExchange(exchangeName)
//                .durable(true)
//                .build();
//    }
//
//    @Bean
//    public Binding binding() {
//        return BindingBuilder.bind(queue())
//                .to(exchange())
//                .with(routingKey);
//    }
//
//    @Bean
//    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
//        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
//        admin.setAutoStartup(true);
//        return admin;
//    }
//
//    @Bean
//    public AbstractMessageConverter messageConverter() {
//        return new JacksonJsonMessageConverter();
//    }
//
//}
