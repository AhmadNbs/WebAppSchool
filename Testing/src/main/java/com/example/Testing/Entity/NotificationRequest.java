package com.example.Testing.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.UuidGenerator;

@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="notification")
public class NotificationRequest {

    @Id
    @UuidGenerator
    @Column(name="email_id")
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String subject;

    @NotNull
    @Email(message = "Invalid Email Format!!")
    private String email;

    @NotBlank
    private String body;
}
