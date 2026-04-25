package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.response.ResMark;
import com.example.bolalarakademiyasi.entity.Mark;
import org.springframework.stereotype.Component;

@Component
public class MarkMapper {
    public ResMark toDTO(Mark mark) {
        return ResMark.builder()
                .markId(mark.getId())
                .markCategoryStatus(mark.getMarkCategoryStatus() != null ? mark.getMarkCategoryStatus().name() : null)
                .totalScore(mark.getTotalScore())
                .studentId(mark.getStudent() != null ? mark.getStudent().getId() : null)
                .studentFirstName(mark.getStudent() != null ? mark.getStudent().getFirstName() : null)
                .studentLastName(mark.getStudent() != null ? mark.getStudent().getLastName() : null)
                .markDate(mark.getDate())
                .build();
    }


    public ResMark toMarkDTO(Mark mark) {
        return ResMark.builder()
                .markId(mark.getId())
                .studentId(mark.getStudent() != null ? mark.getStudent().getId() : null)
                .studentFirstName(mark.getStudent() != null ? mark.getStudent().getFirstName() : null)
                .studentLastName(mark.getStudent() != null ? mark.getStudent().getLastName() : null)
                .imageUrl(mark.getStudent() != null ? mark.getStudent().getImgUrl() : null)
                .totalScore(mark.getTotalScore())
                .activityScore(mark.getActiveScore()!= null ? mark.getActiveScore() : 0)
                .homeworkScore(mark.getHomeworkScore() != null ? mark.getHomeworkScore() : 0)
                .markCategoryStatus(mark.getMarkCategoryStatus() != null ? mark.getMarkCategoryStatus().name() : null)
                .markDate(mark.getDate() != null ? mark.getDate() : null)
                .build();
    }



    public ResMark toFullDTO(Mark mark) {
        return ResMark.builder()
                .markId(mark.getId())
                .markCategoryStatus(mark.getMarkCategoryStatus() != null ? mark.getMarkCategoryStatus().name() : null)
                .totalScore(mark.getTotalScore())
                .studentId(mark.getStudent() != null ? mark.getStudent().getId() : null)
                .studentFirstName(mark.getStudent() != null ? mark.getStudent().getFirstName() : null)
                .studentLastName(mark.getStudent() != null ? mark.getStudent().getLastName() : null)
                .activityScore(mark.getActiveScore())
                .homeworkScore(mark.getHomeworkScore())
                .markDate(mark.getDate())
                .build();
    }
}
