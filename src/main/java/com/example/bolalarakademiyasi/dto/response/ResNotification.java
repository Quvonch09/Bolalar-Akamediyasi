package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ResNotification {
    private UUID id;
    private String message;
    private String description;
    private UUID studentId;
    private UUID parentId;
    private boolean isRead;
}
