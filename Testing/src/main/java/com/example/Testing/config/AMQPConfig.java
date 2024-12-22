package com.example.Testing.config;


import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;

/**
 * Configuration class for setting up RabbitMQ messaging.
 */
@Configuration
public class AMQPConfig {

    public static final String EXCHANGE_NAME = "email-exchange";
    public static final String QUEUE_NAME = "email-queue";
    public static final String ROUTING_KEY = "email-routing-key";

    /**
     * Define the email queue.
     *
     * @return A new Queue instance for email messages.
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(QUEUE_NAME, false);
    }

    /**
     * Define the direct exchange.
     *
     * @return A new DirectExchange instance.
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    /**
     * Bind the email queue to the exchange with a routing key.
     *
     * @param queue    The Queue instance.
     * @param exchange The DirectExchange instance.
     * @return A new Binding instance.
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * Define the RabbitTemplate with a JSON message converter.
     *
     * @param connectionFactory The ConnectionFactory instance.
     * @param messageConverter  The Jackson2JsonMessageConverter instance.
     * @return A new RabbitTemplate instance.
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    /**
     * Define the Jackson2JsonMessageConverter bean.
     *
     * @return A new Jackson2JsonMessageConverter instance.
     */
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
