package com.example.bolalarakademiyasi.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResSinfDTO {

    private UUID id;

    private String name;

    private UUID teacherId;

    @Schema(hidden = true)
    private String teacherFirstName;

    @Schema(hidden = true)
    private String teacherLastName;
}
