package com.example.microService.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Setter
@Getter
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="email")
public class Email {
    @Id
    @UuidGenerator
    @Column(name="email_id")
    private UUID id;

    private String name;

    private String subject;

    private String email;

    private String body;
}
