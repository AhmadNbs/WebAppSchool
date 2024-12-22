package com.example.Testing.validators;

import com.example.Testing.Entity.Note;
import com.example.Testing.Repository.NoteRepository;
import org.apache.velocity.exception.ResourceNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class NoteValidator {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoteValidator.class);

    public static void validateNoteId(UUID id, NoteRepository noteRepository) {
        Optional<Note> noteOptional = noteRepository.findById(id);
        if (noteOptional.isEmpty()) {
            LOGGER.error("Mark with id {} was not found in db", id);
            throw new ResourceNotFoundException(Note.class.getSimpleName() + " with id: " + id);
        }
    }
}
