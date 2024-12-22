package com.example.Testing.DTOS;

import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.User;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO class representing note data.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NoteDTO {
    private UUID id; // The unique identifier of the note.
    private float nota; // The grade or score associated with the note.
    private List<Materia> subjects; // The list of subjects or courses associated with the note.
    private List<User> users; // The list of users associated with the note.
}
