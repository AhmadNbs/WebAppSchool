package com.example.Testing.DTOS;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationRequestDTO implements Serializable {
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
