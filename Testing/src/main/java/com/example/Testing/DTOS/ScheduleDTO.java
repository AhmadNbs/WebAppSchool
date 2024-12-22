package com.example.Testing.DTOS;

import com.example.Testing.Entity.Clasa;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.User;
import lombok.*;

import java.util.UUID;

/**
 * DTO class representing schedule data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ScheduleDTO {
    private UUID id; // The unique identifier of the schedule.
    private String day; // The day of the week for the schedule.
    private String time; // The time of the schedule.
    private Materia materia; // The subject or course associated with the schedule.
    private User user; // The user associated with the schedule, typically a professor.
    private Clasa clasa; // The class associated with the schedule.
}
