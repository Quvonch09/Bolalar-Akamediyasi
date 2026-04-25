package com.example.bolalarakademiyasi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReqNotification {

    private String message;
    private String description;
    private UUID studentId;
    private UUID parentId;
}
