package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.response.ResNotification;
import com.example.bolalarakademiyasi.entity.Notification;
import org.springframework.stereotype.Component;

@Component
public class NotificationMapper {

    public ResNotification toNotificationDTO(Notification notif) {
        return ResNotification.builder()
                .id(notif.getId())
                .message(notif.getMessage())
                .description(notif.getDescription())
                .studentId(notif.getStudent() != null ? notif.getStudent().getId() : null)
                .parentId(notif.getParent() != null ? notif.getParent().getId() : null)
                .isRead(notif.isRead())
                .build();
    }
}
