package com.example.bolalarakademiyasi.dto.request;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqSubmission {

    private String textAnswer;

    private String fileUrl;


    private String feedback;

    private UUID studentId;

    private UUID homeworkId;
}
