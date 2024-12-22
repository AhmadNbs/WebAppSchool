package com.example.Testing.config;

import com.example.Testing.Entity.NotificationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for RestTemplate and email sending functionality.
 */
@Configuration
public class RestTemplateConfig {

    public static final String FIRST_TOKEN = "a1b2c3d4-5f6g-7h8i-9j0k-1l2m3n4o5p6q";
    public static final String LAST_TOKEN = "r7s8t9u0-v1w2-x3y4-z5a6-b7c8d9e0f1g2";
    public static final String authToken = FIRST_TOKEN + LAST_TOKEN;

    private final ObjectMapper mapper;

    public RestTemplateConfig(ObjectMapper objectMapper) {
        this.mapper = objectMapper;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * Sends an email notification using the provided NotificationRequest DTO and RestTemplate.
     *
     * @param notificationRequestDto The NotificationRequest object representing the email notification.
     * @param restTemplate           The RestTemplate object used to make the HTTP request.
     */
    public void sendEmail(NotificationRequest notificationRequestDto, RestTemplate restTemplate) {
        try {
            String jsonPayload = mapper.writeValueAsString(notificationRequestDto);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(authToken);

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8081/api/notification/sender",
                    HttpMethod.POST,
                    requestEntity,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                System.out.println("Email successfully sent!");
            } else {
                System.out.println("Failed email to be sent. Response: " + response.getBody());
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}