package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.request.ReqHomework;
import com.example.bolalarakademiyasi.dto.response.ResHomework;
import com.example.bolalarakademiyasi.entity.Homework;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor


public class HomeworkMapper {


    public ResHomework toResHomework(Homework homework) {
        return ResHomework.builder()
                .id(homework.getId())
                .title(homework.getTitle())
                .description(homework.getDescription())
                .deadline(homework.getDeadline())
                .lessonID(homework.getLesson() != null ? homework.getLesson().getId() : null)
                .build();

    }


}
