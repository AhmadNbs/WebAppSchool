package com.example.Testing.DTOS.Builders;

import com.example.Testing.DTOS.ClasaDTO;
import com.example.Testing.Entity.Clasa;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Builder class for creating instances of ClasaDTO and converting between Clasa and ClasaDTO.
 */
@NoArgsConstructor
@Builder
public class ClasaBuilder {

    /**
     * Converts a Clasa entity to a ClasaDTO.
     *
     * @param clasa The Clasa entity to convert.
     * @return The corresponding ClasaDTO.
     */
    public static ClasaDTO toClasaDTO(Clasa clasa) {
        return ClasaDTO.builder()
                .id(clasa.getId())
                .grade(clasa.getGrade())
                .number(clasa.getNumber())
                .users(clasa.getUsers())
                .schedules(clasa.getSchedules())
                .build();
    }

    /**
     * Converts a ClasaDTO to a Clasa entity.
     *
     * @param clasaDTO The ClasaDTO to convert.
     * @return The corresponding Clasa entity.
     */
    public static Clasa toEntity(ClasaDTO clasaDTO) {
        return Clasa.builder()
                .id(clasaDTO.getId())
                .grade(clasaDTO.getGrade())
                .number(clasaDTO.getNumber())
                .users(clasaDTO.getUsers())
                .schedules(clasaDTO.getSchedules())
                .build();
    }
}
