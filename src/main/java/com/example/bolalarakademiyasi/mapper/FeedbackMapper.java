package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.response.ResFeedback;
import com.example.bolalarakademiyasi.dto.response.ResFeedbackDTO;
import com.example.bolalarakademiyasi.entity.Feedback;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor


public class FeedbackMapper {


    public ResFeedback toResponse(Feedback feedback) {
        return ResFeedback.builder()
                .id(feedback.getId())
                .title(feedback.getTitle())
                .rating(feedback.getRating())
                .teacherId(feedback.getTeacher() != null ? feedback.getTeacher().getId() : null)
                .teacherName(feedback.getTeacher() != null ? feedback.getTeacher().getFirstName() : null)
                .teacherName(feedback.getTeacher() != null ? feedback.getTeacher().getLastName() : null)
                .build();
    }



}
