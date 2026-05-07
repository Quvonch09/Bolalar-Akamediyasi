package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.response.ResVideoProgress;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.VideoLesson;
import com.example.bolalarakademiyasi.entity.VideoProgress;
import org.springframework.stereotype.Component;

@Component
public class VideoProgressMapper {

    public ResVideoProgress toResponse(VideoProgress videoProgress) {
        Student student = videoProgress.getStudent();
        VideoLesson videoLesson = videoProgress.getVideoLesson();
        Lesson lesson = videoLesson != null ? videoLesson.getLesson() : null;

        return ResVideoProgress.builder()
                .id(videoProgress.getId())
                .watchedSeconds(videoProgress.getWatchedSeconds())
                .lastPositionSeconds(videoProgress.getLastPositionSeconds())
                .completed(Boolean.TRUE.equals(videoProgress.getCompleted()))
                .progressPercent(calculateProgress(videoProgress))
                .lastWatchedAt(videoProgress.getLastWatchedAt())
                .studentId(student != null ? student.getId() : null)
                .studentName(student != null ? buildStudentName(student) : null)
                .videoLessonId(videoLesson != null ? videoLesson.getId() : null)
                .videoLessonTitle(videoLesson != null ? videoLesson.getTitle() : null)
                .lessonId(lesson != null ? lesson.getId() : null)
                .lessonName(lesson != null ? lesson.getName() : null)
                .build();
    }

    private Double calculateProgress(VideoProgress videoProgress) {
        if (videoProgress.getVideoLesson() == null || videoProgress.getVideoLesson().getDuration() == null) {
            return null;
        }

        int duration = videoProgress.getVideoLesson().getDuration();
        if (duration <= 0 || videoProgress.getWatchedSeconds() == null) {
            return 0D;
        }

        double percent = (videoProgress.getWatchedSeconds() * 100.0) / duration;
        return Math.min(100D, percent);
    }

    private String buildStudentName(Student student) {
        String lastName = student.getLastName() == null ? "" : " " + student.getLastName();
        return student.getFirstName() + lastName;
    }
}
