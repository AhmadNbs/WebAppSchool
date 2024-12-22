package com.example.Testing.validators;

import com.example.Testing.Entity.Materia;
import com.example.Testing.Entity.Note;
import com.example.Testing.Repository.MateriaRepository;
import com.example.Testing.Repository.NoteRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class MateriaValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(MateriaValidator.class);

    public static void validateMateriaId(UUID id, MateriaRepository materiaRepository) {
        Optional<Materia> materiaOptional = materiaRepository.findById(id);
        if (materiaOptional.isEmpty()) {
            LOGGER.error("Materia with id {} was not found in db", id);
            throw new ResourceNotFoundException(Materia.class.getSimpleName() + " with id: " + id);
        }
    }
}
