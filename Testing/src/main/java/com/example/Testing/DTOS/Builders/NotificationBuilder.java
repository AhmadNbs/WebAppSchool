package com.example.Testing.DTOS.Builders;

import com.example.Testing.DTOS.NotificationRequestDTO;
import com.example.Testing.Entity.NotificationRequest;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
public class NotificationBuilder {

    public static NotificationRequestDTO toNotificationDTO(NotificationRequest notification) {
        return NotificationRequestDTO.builder()
                .id(notification.getId())
                .name(notification.getName())
                .subject(notification.getSubject())
                .email(notification.getEmail())
                .body(notification.getBody())
                .build();
    }

    public static NotificationRequest toEntity(NotificationRequestDTO notificationDTO) {
        return NotificationRequest.builder()
                .id(notificationDTO.getId())
                .name(notificationDTO.getName())
                .subject(notificationDTO.getSubject())
                .email(notificationDTO.getEmail())
                .body(notificationDTO.getBody())
                .build();
    }
}
