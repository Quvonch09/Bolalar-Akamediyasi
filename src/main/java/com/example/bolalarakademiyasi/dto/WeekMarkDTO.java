package com.example.bolalarakademiyasi.dto;

import com.example.bolalarakademiyasi.entity.enums.MarkCategoryStatus;
import com.example.bolalarakademiyasi.entity.enums.WeekDays;
import lombok.*;

import java.time.LocalDate;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WeekMarkDTO {
    private WeekDays day;
    private Integer score;                  // 9
    private MarkCategoryStatus category;    // YASHIL/SARIQ/QIZIL
    private LocalDate date;
}