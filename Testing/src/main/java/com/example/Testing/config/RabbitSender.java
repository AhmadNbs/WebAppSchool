package com.example.Testing.config;

import com.example.Testing.DTOS.NotificationRequestDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.Testing.config.AMQPConfig.EXCHANGE_NAME;
import static com.example.Testing.config.AMQPConfig.ROUTING_KEY;

/**
 * Component class responsible for sending messages to RabbitMQ.
 */
@Component
public class RabbitSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * Sends a notification payload to RabbitMQ.
     *
     * @param payload The NotificationRequestDTO object representing the notification payload.
     */
    public void send(NotificationRequestDTO payload) {
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, payload);
    }

}
