package com.example.bolalarakademiyasi.mapper;
import com.example.bolalarakademiyasi.dto.response.ResLesson;
import com.example.bolalarakademiyasi.entity.Lesson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LessonMapper {


    public ResLesson toResLesson(Lesson lesson){
        return ResLesson.builder()
                .id(lesson.getId())
                .name(lesson.getName())
                .description(lesson.getDescription())
                .fileUrl(lesson.getFileUrl())
                .subjectId(lesson.getSubject() != null ? lesson.getSubject().getId() : null)
                .subjectName(lesson.getSubject() != null ? lesson.getSubject().getName() : null)
                .build();
    }

}
