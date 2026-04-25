package com.example.bolalarakademiyasi.dto.request;

import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.entity.enums.ShiftEnum;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqClass {

    private String name;

    private ShiftEnum shift;

    private UUID teacherId;




}
