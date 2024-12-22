package com.example.Testing.validators;

import com.example.Testing.Entity.Schedule;
import com.example.Testing.Repository.ScheduleRepository;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.UUID;

public class ScheduleValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleValidator.class);

    public static void validateScheduleId(UUID id, ScheduleRepository scheduleRepository) {
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(id);
        if (scheduleOptional.isEmpty()) {
            LOGGER.error("Schedule with id {} was not found in db", id);
            throw new ResourceNotFoundException(Schedule.class.getSimpleName() + " with id: " + id);
        }
    }
}
