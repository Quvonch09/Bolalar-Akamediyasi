package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqGradeSubmission;
import com.example.bolalarakademiyasi.dto.request.ReqSubmission;
import com.example.bolalarakademiyasi.dto.request.ReqUpdateSubmission;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResSubmission;
import com.example.bolalarakademiyasi.entity.Homework;
import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.Submission;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.SubmissionMapper;
import com.example.bolalarakademiyasi.repository.HomeworkRepository;
import com.example.bolalarakademiyasi.repository.StudentRepository;
import com.example.bolalarakademiyasi.repository.SubmissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubmissionService {

    private final SubmissionRepository submissionRepository;
    private final HomeworkRepository homeworkRepository;
    private final StudentRepository studentRepository;
    private final SubmissionMapper submissionMapper;

    public ApiResponse<String> saveSubmission(ReqSubmission req) {
        Homework homework = homeworkRepository.findByIdAndActiveTrue(req.getHomeworkId()).orElseThrow(
                () -> new DataNotFoundException("Homework not found")
        );

        Student student = studentRepository.findByIdAndActiveTrue(req.getStudentId()).orElseThrow(
                () -> new DataNotFoundException("Student not found")
        );

        Submission submission = Submission.builder()
                .textAnswer(req.getTextAnswer())
                .fileUrl(req.getFileUrl())
                .student(student)
                .homework(homework)
                .build();
        
        submissionRepository.save(submission);
        return ApiResponse.success(null, "Submission saved successfully");
    }



    public ApiResponse<String> gradeSubmission(UUID submissionId, ReqGradeSubmission req) {
        Submission submission = submissionRepository.findByIdAndActiveTrue(submissionId).orElseThrow(
                () -> new DataNotFoundException("Submission not found")
        );

        submission.setScore(req.getScore());
        submission.setFeedback(req.getFeedback());

        submissionRepository.save(submission);
        return ApiResponse.success(null, "Submission graded successfully");
    }



    public ApiResponse<ResPageable> getAllSubmissions(String studentName, String homeworkTitle, Boolean isGraded, int page, int size) {

        Page<Submission> submissions = submissionRepository.searchSubmissions(
                studentName, homeworkTitle, isGraded, PageRequest.of(page, size)
        );

        if (submissions.isEmpty()) {
            throw new DataNotFoundException("Submissions not found");
        }

        List<ResSubmission> list = submissions.getContent().stream()
                .map(submissionMapper::toResSubmission).toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(submissions.getTotalElements())
                .totalPage(submissions.getTotalPages())
                .body(list)
                .build();

        return ApiResponse.success(resPageable, "Success");
    }



    public ApiResponse<String> updateSubmission(UUID submissionId, ReqUpdateSubmission req) {
        Submission submission = submissionRepository.findByIdAndActiveTrue(submissionId).orElseThrow(
                () -> new DataNotFoundException("Submission not found")
        );

        if (req.getTextAnswer() != null) {
            submission.setTextAnswer(req.getTextAnswer());
        }
        if (req.getFileUrl() != null) {
            submission.setFileUrl(req.getFileUrl());
        }

        submissionRepository.save(submission);
        return ApiResponse.success(null, "Submission updated successfully");
    }
    public ApiResponse<String> deleteSubmission(UUID submissionId) {
        Submission submission = submissionRepository.findByIdAndActiveTrue(submissionId).orElseThrow(
                () -> new DataNotFoundException("Submission not found")
        );

        submission.softDelete();
        submissionRepository.save(submission);

        return ApiResponse.success(null, "Submission deleted successfully");
    }
    public ApiResponse<ResPageable> getSubmissionsByHomework(UUID homeworkId, int page, int size) {
        homeworkRepository.findByIdAndActiveTrue(homeworkId).orElseThrow(
                () -> new DataNotFoundException("Homework not found")
        );

        Page<Submission> submissions = submissionRepository.findAllByHomeworkIdAndActiveTrue(
                homeworkId, PageRequest.of(page, size)
        );

        if (submissions.getTotalElements() == 0) {
            throw new DataNotFoundException("Submissions not found for this homework");
        }

        List<ResSubmission> list = submissions.getContent().stream()
                .map(submissionMapper::toResSubmission).toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(submissions.getTotalElements())
                .totalPage(submissions.getTotalPages())
                .body(list)
                .build();

        return ApiResponse.success(resPageable, "Success");
    }

    public ApiResponse<ResSubmission> getOneSubmission(UUID submissionId) {
        Submission submission = submissionRepository.findByIdAndActiveTrue(submissionId).orElseThrow(
                () -> new DataNotFoundException("Submission not found")
        );
        return ApiResponse.success(submissionMapper.toResSubmission(submission), "Success");
    }
}