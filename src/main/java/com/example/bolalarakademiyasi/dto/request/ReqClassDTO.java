package com.example.bolalarakademiyasi.dto.request;

import com.example.bolalarakademiyasi.entity.enums.ShiftEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ReqClassDTO {
    private UUID id;

    private UUID teacherId;

    private String name;

    private ShiftEnum shiftEnum;



}
