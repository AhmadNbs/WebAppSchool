package com.example.microService.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;

/**
 * A configuration class for AMQP (Advanced Message Queuing Protocol).
 */
@Configuration
@ComponentScan(basePackages = "com.example.microservice.repositories")
public class AMQPConfig{

    /**
     * Configures the email queue.
     *
     * @return The configured email queue.
     */

    @Bean
    public Queue emailQueue() {
        return new Queue("email-queue", false);
    }

    /**
     * Configures the direct exchange for email communication.
     *
     * @return The configured direct exchange.
     */

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange("email-exchange");
    }

    /**
     * Binds the email queue to the email exchange with a specific routing key.
     *
     * @param emailQueue    The email queue to bind.
     * @param emailExchange The email exchange to bind to.
     * @return The binding between the email queue and exchange.
     */
    @Bean
    public Binding binding(Queue emailQueue, DirectExchange emailExchange) {
        return BindingBuilder.bind(emailQueue).to(emailExchange).with("email-routing-key");
    }
}