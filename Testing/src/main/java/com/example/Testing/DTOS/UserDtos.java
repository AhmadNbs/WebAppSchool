package com.example.Testing.DTOS;

import com.example.Testing.Entity.Clasa;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.Note;
import com.example.Testing.Entity.Schedule;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * DTO class representing user data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDtos {
    private UUID id; // The unique identifier of the user.
    private String firstName; // The first name of the user.
    private String lastName; // The last name of the user.
    private String password; // The password of the user.
    private String email; // The email address of the user.
    private String phone; // The phone number of the user.
    private String role; // The role of the user (e.g., professor, student).
    private List<Clasa> clase; // The list of classes the user is associated with.
    private List<Schedule> schedules; // The list of schedules associated with the user.
    private Map<Materia,Note> marks;
}
