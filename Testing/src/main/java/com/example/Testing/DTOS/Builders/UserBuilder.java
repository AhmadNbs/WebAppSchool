package com.example.Testing.DTOS.Builders;

import com.example.Testing.DTOS.UserDtos;
import com.example.Testing.Entity.User;
import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * Builder class for converting between User and UserDtos objects.
 */
@NoArgsConstructor
@Builder
public class UserBuilder {

    /**
     * Convert User entity to UserDtos DTO.
     *
     * @param user The User entity to convert.
     * @return UserDtos object representing the converted User entity.
     */
    public static UserDtos toUserDTO(User user) {
        return UserDtos.builder()
                .id(user.getId())
                .role(user.getRole())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .email(user.getEmail())
                .phone(user.getPhone())
                .clase(user.getClase())
                .schedules(user.getSchedules())
                .marks(user.getMarks())
                .build();
    }

    /**
     * Convert UserDtos DTO to User entity.
     *
     * @param userDTO The UserDtos DTO to convert.
     * @return User entity representing the converted UserDtos DTO.
     */
    public static User toEntity(UserDtos userDTO) {
        return User.builder()
                .id(userDTO.getId())
                .role(userDTO.getRole())
                .firstName(userDTO.getFirstName())
                .lastName(userDTO.getLastName())
                .password(userDTO.getPassword())
                .email(userDTO.getEmail())
                .phone(userDTO.getPhone())
                .clase(userDTO.getClase())
                .schedules(userDTO.getSchedules())
                .marks(userDTO.getMarks())
                .build();
    }
}
