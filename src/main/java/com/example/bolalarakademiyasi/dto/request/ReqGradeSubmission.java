package com.example.bolalarakademiyasi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReqGradeSubmission {
    private int score; // Baho (agar butun son bo'lsa Integer qilsangiz ham bo'ladi)
    private String feedback; // O'qituvchi izohi
}