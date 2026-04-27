package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResClass {
    private UUID id;

    private String name;

    private String teacherFirstName;

    private String teacherLastName;

    private Long studentCount;

}
