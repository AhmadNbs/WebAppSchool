package com.example.Testing.DTOS.Builders;

import com.example.Testing.DTOS.ScheduleDTO;
import com.example.Testing.Entity.Schedule;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Builder class for converting between Schedule and ScheduleDTO objects.
 */
@NoArgsConstructor
@Builder
public class ScheduleBuilder {

    /**
     * Convert Schedule entity to ScheduleDTO DTO.
     *
     * @param schedule The Schedule entity to convert.
     * @return ScheduleDTO object representing the converted Schedule entity.
     */
    public static ScheduleDTO toScheduleDTO(Schedule schedule) {
        return ScheduleDTO.builder()
                .id(schedule.getId())
                .day(schedule.getDay())
                .time(schedule.getTime())
                .materia(schedule.getMateria())
                .user(schedule.getUser())
                .clasa(schedule.getClasa())
                .build();
    }

    /**
     * Convert ScheduleDTO DTO to Schedule entity.
     *
     * @param scheduleDTO The ScheduleDTO DTO to convert.
     * @return Schedule entity representing the converted ScheduleDTO DTO.
     */
    public static Schedule toEntity(ScheduleDTO scheduleDTO) {
        return Schedule.builder()
                .id(scheduleDTO.getId())
                .day(scheduleDTO.getDay())
                .time(scheduleDTO.getTime())
                .materia(scheduleDTO.getMateria())
                .user(scheduleDTO.getUser())
                .clasa(scheduleDTO.getClasa())
                .build();
    }
}
