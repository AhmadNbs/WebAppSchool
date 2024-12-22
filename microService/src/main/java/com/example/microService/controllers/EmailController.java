package com.example.microService.controllers;

import com.example.microService.dtos.MessageDTO;
import com.example.microService.dtos.NotificationRequestDTO;
import com.example.microService.services.EmailService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


/**
 * A controller class responsible for handling email notification requests.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class EmailController {
    private final EmailService emailService;
    private static final String FIRST_TOKEN = "a1b2c3d4-5f6g-7h8i-9j0k-1l2m3n4o5p6q";
    private static final String LAST_TOKEN = "r7s8t9u0-v1w2-x3y4-z5a6-b7c8d9e0f1g2";

    /**
     * Handles POST requests to "/api/notification/sender" for sending email notifications.
     *
     * @param authorizationHeader The authorization token in the request header.
     * @param notificationRequest The request body containing the notification details.
     * @param bindingResult       The result of the validation of the request body.
     * @return A ResponseEntity containing a MessageDTO indicating the status of the email sending process.
     */

    @PostMapping("/sender")
    public ResponseEntity<MessageDTO> handleEmailRequest(
            @RequestHeader("Authorization") String authorizationHeader,
            @Valid @RequestBody NotificationRequestDTO notificationRequest,
            BindingResult bindingResult) {

        if (notificationRequest == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO("Invalid request payload"));
        }

        if (!isTokenValid(authorizationHeader)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageDTO("Unauthorized: Invalid token"));
        }

        if (bindingResult.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageDTO(bindingResult.getFieldError().getDefaultMessage()));
        }

        emailService.sendEmail(notificationRequest);
        return ResponseEntity.ok(new MessageDTO("Email has been sent successfully!"));
    }

    /**
     * Checks if the provided authorization token is valid.
     *
     * @param token The authorization token to be validated.
     * @return {@code true} if the token is valid, {@code false} otherwise.
     */

    private boolean isTokenValid(String token) {
        String[] tokenParts = token.split(" ");
        if (tokenParts.length != 2) {
            return false;
        }
        String receivedToken = tokenParts[1];
        String expectedToken = FIRST_TOKEN + LAST_TOKEN;
        return expectedToken.equals(receivedToken);
    }
}
