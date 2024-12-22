package com.example.Testing.Repository;

import com.example.Testing.Entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Repository interface for managing Schedule entities.
 */
@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {
}
