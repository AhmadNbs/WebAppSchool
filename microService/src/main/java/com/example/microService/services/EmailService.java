package com.example.microService.services;

import com.example.microService.dtos.NotificationRequestDTO;
import com.example.microService.entities.Email;
import com.example.microService.repository.EmailRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.validation.Valid;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Service class for sending emails.
 */
@Service
public class EmailService {
    private final EmailRepository emailRepo;
    private final JavaMailSender emailSender;
    private final ObjectMapper mapper;

    public EmailService(EmailRepository emailRepository, JavaMailSender emailSender, ObjectMapper mapper) {
        this.emailRepo = emailRepository;
        this.emailSender = emailSender;
        this.mapper = mapper;
    }

    /**
     * Load the email template from the resources.
     *
     * @return The loaded email template as a string.
     * @throws IOException If an I/O error occurs.
     */

    private String loadEmailTemplate() throws IOException {
        return StreamUtils.copyToString(
                new ClassPathResource("templates/sender.html").getInputStream(),
                StandardCharsets.UTF_8
        );
    }

    /**
     * Send an email using the provided NotificationRequestDTO.
     *
     * @param notification The NotificationRequestDTO object representing the email notification.
     */

    public void sendEmail(@Valid NotificationRequestDTO notification)
    {
        try {
            String jsonPayload = mapper.writeValueAsString(notification);
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("ahmadspring26@gmail.com", "Al-Saadeh School");
            helper.setTo(notification.getEmail());
            helper.setSubject(notification.getSubject());

            String htmlTemplate = loadEmailTemplate();
            String modifiedHtmlContent = htmlTemplate.replace("$BODY_CONTENT$", notification.getBody());
            helper.setText(modifiedHtmlContent, true);

            emailSender.send(message);

            Email email = new Email();
            email.setSubject(notification.getSubject());
            email.setName(notification.getName());
            email.setBody(notification.getBody());
            email.setEmail(notification.getEmail());

            emailRepo.save(email);
        }catch(MessagingException | IOException e)
        {
            e.printStackTrace();
        }
    }
}
