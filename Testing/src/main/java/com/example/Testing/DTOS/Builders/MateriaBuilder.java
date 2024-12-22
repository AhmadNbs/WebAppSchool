package com.example.Testing.DTOS.Builders;

import com.example.Testing.DTOS.MateriaDTO;
import com.example.Testing.Entity.Materia;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Builder class for converting between Materia and MateriaDTO objects.
 */
@NoArgsConstructor
@Builder
public class MateriaBuilder {

    /**
     * Convert Materia entity to MateriaDTO DTO.
     *
     * @param materia The Materia entity to convert.
     * @return MateriaDTO object representing the converted Materia entity.
     */
    public static MateriaDTO toMateriaDTO(Materia materia) {
        return MateriaDTO.builder()
                .id(materia.getId())
                .name(materia.getName())
                .schedules(materia.getSchedules())
                .marks(materia.getMarks())
                .build();
    }

    /**
     * Convert MateriaDTO DTO to Materia entity.
     *
     * @param materiaDTO The MateriaDTO DTO to convert.
     * @return Materia entity representing the converted MateriaDTO DTO.
     */
    public static Materia toEntity(MateriaDTO materiaDTO) {
        return Materia.builder()
                .id(materiaDTO.getId())
                .name(materiaDTO.getName())
                .schedules(materiaDTO.getSchedules())
                .marks(materiaDTO.getMarks())
                .build();
    }
}
