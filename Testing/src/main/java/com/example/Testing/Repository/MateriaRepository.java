package com.example.Testing.Repository;

import com.example.Testing.Entity.Materia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing Materia entities.
 */
@Repository
public interface MateriaRepository extends JpaRepository<Materia, UUID> {
}
