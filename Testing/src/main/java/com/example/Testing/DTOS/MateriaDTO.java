package com.example.Testing.DTOS;

import com.example.Testing.Entity.Note;
import com.example.Testing.Entity.Schedule;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * DTO class representing subject or course data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MateriaDTO {
    private UUID id; // The unique identifier of the subject or course.
    private String name; // The name of the subject or course.
    private List<Schedule> schedules; // The list of schedules associated with the subject or course.
    private List<Note> marks; // The list of notes or grades associated with the subject or course.
}
