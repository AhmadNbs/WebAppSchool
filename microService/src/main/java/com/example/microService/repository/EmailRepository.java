package com.example.microService.repository;

import com.example.microService.entities.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Repository interface for interacting with the Email entity.
 */
@Repository
public interface EmailRepository extends JpaRepository<Email,String> {
}
