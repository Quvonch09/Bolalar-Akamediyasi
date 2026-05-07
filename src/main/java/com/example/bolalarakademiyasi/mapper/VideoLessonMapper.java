package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.response.ResVideoLesson;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.VideoLesson;
import org.springframework.stereotype.Component;

@Component
public class VideoLessonMapper {

    public ResVideoLesson toResponse(VideoLesson videoLesson) {
        Lesson lesson = videoLesson.getLesson();

        return ResVideoLesson.builder()
                .id(videoLesson.getId())
                .title(videoLesson.getTitle())
                .videoUrl(videoLesson.getVideoUrl())
                .duration(videoLesson.getDuration())
                .fileSize(videoLesson.getFileSize())
                .format(videoLesson.getFormat())
                .lessonId(lesson != null ? lesson.getId() : null)
                .lessonName(lesson != null ? lesson.getName() : null)
                .subjectId(lesson != null && lesson.getSubject() != null ? lesson.getSubject().getId() : null)
                .subjectName(lesson != null && lesson.getSubject() != null ? lesson.getSubject().getName() : null)
                .build();
    }
}
