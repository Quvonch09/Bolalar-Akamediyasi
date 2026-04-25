package com.example.bolalarakademiyasi.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResTeacher {
    private UUID id;
    private String firstName;
    private String lastName;
    private String phone;
    private String imageUrl;
    private List<ResStudent> studentList;
    private List<ResSinfDTO> sinfDTOList;
}
