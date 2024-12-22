package com.example.Testing.Repository;

import com.example.Testing.Entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing Note entities.
 */
@Repository
public interface NoteRepository extends JpaRepository<Note, UUID> {
}
