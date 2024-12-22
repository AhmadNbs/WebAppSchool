package com.example.microService.listener;

import com.example.microService.dtos.NotificationRequestDTO;
import com.example.microService.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ message listener for processing email notification requests.
 */
@Component
public class EmailQueueListener {

    private final EmailService emailService;
    private final ObjectMapper mapper;

    @Autowired
    public EmailQueueListener(EmailService emailService, ObjectMapper objectMapper) {
        this.emailService = emailService;
        this.mapper = objectMapper;
    }

    /**
     * RabbitMQ message listener method to handle email notification requests.
     * It consumes messages from the "email-queue" queue.
     *
     * @param message The message received from the queue.
     */

    @RabbitListener(queues = "email-queue")
    public void handleEmailRequest(String message) {
        try {

            NotificationRequestDTO notification = mapper.readValue(message, NotificationRequestDTO.class);
            System.out.println(notification);

            emailService.sendEmail(notification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
