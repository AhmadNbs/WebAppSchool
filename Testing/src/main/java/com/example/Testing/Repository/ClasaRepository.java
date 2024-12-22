package com.example.Testing.Repository;

import com.example.Testing.Entity.Clasa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing Clasa entities.
 */
@Repository
public interface ClasaRepository extends JpaRepository<Clasa, UUID> {
}
