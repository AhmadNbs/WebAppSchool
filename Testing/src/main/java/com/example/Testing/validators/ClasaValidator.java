package com.example.Testing.validators;

import com.example.Testing.Entity.Clasa;
import com.example.Testing.Entity.Materia;
import com.example.Testing.Repository.ClasaRepository;
import com.example.Testing.Repository.MateriaRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class ClasaValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ClasaValidator.class);

    public static void validateClasaId(UUID id, ClasaRepository clasaRepository) {
        Optional<Clasa> clasaOptional = clasaRepository.findById(id);
        if (clasaOptional.isEmpty()) {
            LOGGER.error("Clasa with id {} was not found in db", id);
            throw new ResourceNotFoundException(Clasa.class.getSimpleName() + " with id: " + id);
        }
    }
}
