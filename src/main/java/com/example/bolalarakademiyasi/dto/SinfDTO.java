package com.example.bolalarakademiyasi.dto;


import com.example.bolalarakademiyasi.dto.response.ResStudent;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SinfDTO {

    @Schema(hidden = true)
    private UUID id;

    private String name;

    private UUID teacherId;

    @Schema(hidden = true)
    private String teacherFirstName;

    @Schema(hidden = true)
    private String teacherLastName;


    @Schema(hidden = true)
    private List<ResStudent> students;
}
