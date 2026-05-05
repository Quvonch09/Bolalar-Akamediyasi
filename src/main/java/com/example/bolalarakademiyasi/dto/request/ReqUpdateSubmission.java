package com.example.bolalarakademiyasi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqUpdateSubmission {
    private String textAnswer;
    private String fileUrl;
}