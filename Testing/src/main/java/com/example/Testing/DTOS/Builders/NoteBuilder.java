package com.example.Testing.DTOS.Builders;

import com.example.Testing.DTOS.NoteDTO;
import com.example.Testing.Entity.Note;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Builder class for converting between Note and NoteDTO objects.
 */
@NoArgsConstructor
@Builder
public class NoteBuilder {

    /**
     * Convert Note entity to NoteDTO DTO.
     *
     * @param note The Note entity to convert.
     * @return NoteDTO object representing the converted Note entity.
     */
    public static NoteDTO toNoteDTO(Note note) {
        return NoteDTO.builder()
                .id(note.getId())
                .nota(note.getNota())
                .subjects(note.getSubjects())
                .users(note.getUsers())
                .build();
    }

    /**
     * Convert NoteDTO DTO to Note entity.
     *
     * @param noteDTO The NoteDTO DTO to convert.
     * @return Note entity representing the converted NoteDTO DTO.
     */
    public static Note toEntity(NoteDTO noteDTO) {
        return Note.builder()
                .id(noteDTO.getId())
                .nota(noteDTO.getNota())
                .subjects(noteDTO.getSubjects())
                .users(noteDTO.getUsers())
                .build();
    }
}
