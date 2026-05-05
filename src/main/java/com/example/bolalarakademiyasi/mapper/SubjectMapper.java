package com.example.bolalarakademiyasi.mapper;


import com.example.bolalarakademiyasi.dto.response.ResLesson;
import com.example.bolalarakademiyasi.dto.response.ResSubject;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.Subject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubjectMapper {


    public ResSubject toResSubject(Subject subject) {
        return ResSubject.builder()
                .id(subject.getId())
                .name(subject.getName())
                .description(subject.getDescription())
                .build();
    }



    }
