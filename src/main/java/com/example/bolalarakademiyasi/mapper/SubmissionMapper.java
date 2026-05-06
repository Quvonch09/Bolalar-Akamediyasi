package com.example.bolalarakademiyasi.mapper;


import com.example.bolalarakademiyasi.dto.response.ResSubmission;
import com.example.bolalarakademiyasi.entity.Subject;
import com.example.bolalarakademiyasi.entity.Submission;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SubmissionMapper {


    public ResSubmission toResSubmission(Submission submission) {
        return ResSubmission.builder()
                .id(submission.getId())
                .textAnswer(submission.getTextAnswer())
                .feedback(submission.getFeedback())
                .score(submission.getScore())
                .fileUrl(submission.getFileUrl())
                .studentId(submission.getStudent() != null ? submission.getStudent().getId() : null)
                .studentName(submission.getStudent() != null ? submission.getStudent().getFirstName() : null)
                .studentName(submission.getStudent() != null ? submission.getStudent().getLastName() : null)
                .homeworkId(submission.getHomework() != null ? submission.getHomework().getId() : null)
                .homeworkName(submission.getHomework() != null ? submission.getHomework().getTitle() : null)

                .build();
    }
}
