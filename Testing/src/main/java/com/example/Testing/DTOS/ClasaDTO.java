package com.example.Testing.DTOS;

import com.example.Testing.Entity.Schedule;
import com.example.Testing.Entity.User;
import lombok.*;

import java.util.List;
import java.util.UUID;

/**
 * Data Transfer Object (DTO) representing a class.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClasaDTO {

    /**
     * The unique identifier for the class.
     */
    private UUID id;

    /**
     * The grade level of the class.
     */
    private int grade;

    /**
     * The class number.
     */
    private int number;

    /**
     * The list of users in the class.
     */
    private List<User> users; // The list of users associated with the class.

    /**
     * The list of schedules associated with the class.
     */
    private List<Schedule> schedules; // The list of schedules associated with the class.

}
