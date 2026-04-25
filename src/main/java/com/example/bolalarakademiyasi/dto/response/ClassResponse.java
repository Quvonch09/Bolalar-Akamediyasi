package com.example.bolalarakademiyasi.dto.response;


import com.example.bolalarakademiyasi.entity.enums.ShiftEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClassResponse {

    private UUID id;

    private String name;

    private UUID tenantId;

    private ShiftEnum shiftEnum;


}
