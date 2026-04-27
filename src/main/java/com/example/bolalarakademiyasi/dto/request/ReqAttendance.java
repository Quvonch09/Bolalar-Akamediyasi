package com.example.bolalarakademiyasi.dto.request;

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
public class ReqAttendance {

    private UUID studentId;

    private AttendaceEnum status;

    private Integer homeworkScore;

    private Integer activityScore;

    private Integer behaviourScore;

    private String description;

    private LocalDate date;
}
