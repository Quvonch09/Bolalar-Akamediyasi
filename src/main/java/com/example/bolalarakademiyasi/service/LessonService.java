package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqFeedback;
import com.example.bolalarakademiyasi.dto.request.ReqLesson;
import com.example.bolalarakademiyasi.dto.response.ResLesson;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.entity.Feedback;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.Subject;
import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.LessonMapper;
import com.example.bolalarakademiyasi.repository.LessonRepository;
import com.example.bolalarakademiyasi.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class LessonService {
    private  final LessonRepository lessonRepository;
    private  final SubjectRepository subjectRepository;
    private final LessonMapper lessonMapper;


    public ApiResponse<String> saveLesson(ReqLesson reqLesson){
        Subject subject = subjectRepository.findById(reqLesson.getSubjectId())
                .orElseThrow( () -> new DataNotFoundException("Subject not found"));

        Lesson lesson = Lesson.builder()
                .name(reqLesson.getName())
                .description(reqLesson.getDescription())
                .fileUrl(reqLesson.getFileUrl())
                .subject(subject)
                .build();
        lessonRepository.save(lesson);
        return ApiResponse.success(null, "Lesson saved successfully");
    }

    public  ApiResponse<String> deleteLesson(UUID lessonId){
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new DataNotFoundException("Lesson not found"));
        lesson.softDelete();
        lessonRepository.save(lesson);
        return ApiResponse.success(null, "Lesson deleted successfully");
    }

    public ApiResponse<ResLesson> getOneLesson(UUID lessonId){
        Lesson lesson = lessonRepository.findByIdAndActiveTrue(lessonId).orElseThrow(
                () -> new DataNotFoundException("Lesson not found")
        );

        return ApiResponse.success(lessonMapper.toResLesson(lesson), "Success");
    }

    public ApiResponse<String> updateLesson(UUID lessonId, ReqLesson reqLesson) {
        Lesson lesson = lessonRepository.findById(lessonId).orElseThrow(
                () -> new DataNotFoundException("Lesson not found")
        );

        Subject subject = subjectRepository.findById(reqLesson.getSubjectId()).orElseThrow(
                () -> new DataNotFoundException("Subject not found")
        );

        lesson.setName(reqLesson.getName());
        lesson.setDescription(reqLesson.getDescription());
        lesson.setFileUrl(reqLesson.getFileUrl());
        lesson.setSubject(subject);

        lessonRepository.save(lesson);
        return ApiResponse.success(null, "Lesson updated successfully");
    }


    public ApiResponse<ResPageable> searchLesson(String name, String subjectName, int page, int size) {
        Page<Lesson> lessonPage = lessonRepository.searchLesson(
                name, subjectName, PageRequest.of(page, size));
        if (lessonPage.getTotalElements() == 0) {
            throw new DataNotFoundException("Lesson not found");
        }

        List<ResLesson> list = lessonPage.getContent()
                .stream()
                .map(lessonMapper::toResLesson)
                .toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(lessonPage.getTotalElements())
                .totalPage(lessonPage.getTotalPages())
                .body(list)
                .build();
        return ApiResponse.success(resPageable, "Success");

    }




}
