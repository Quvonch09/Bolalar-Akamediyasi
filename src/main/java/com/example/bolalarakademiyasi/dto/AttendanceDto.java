package com.example.bolalarakademiyasi.dto;

import com.example.bolalarakademiyasi.entity.enums.AttendaceEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AttendanceDto {

    @Schema(hidden = true)
    private UUID id;

    @Schema(hidden = true)
    private String firstName;

    private String lastName;

    private UUID studentId;

    private AttendaceEnum status;

    private String description;

    private LocalDate date;
}
