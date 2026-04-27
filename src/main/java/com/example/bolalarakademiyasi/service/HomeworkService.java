package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqHomework;
import com.example.bolalarakademiyasi.dto.response.ResHomework;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.entity.Homework;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.HomeworkMapper;
import com.example.bolalarakademiyasi.repository.HomeworkRepository;
import com.example.bolalarakademiyasi.repository.LessonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HomeworkService {

    private final LessonRepository lessonRepository;
    private final HomeworkRepository homeworkRepository;
    private final HomeworkMapper homeworkMapper;


    public ApiResponse<String> saveHomework(ReqHomework reqHomework) {
        Lesson lesson = lessonRepository.findById(reqHomework.getLessonId()).orElseThrow(
                ()-> new DataNotFoundException("Lesson not found")
        );

        Homework homework = Homework.builder()
                .title(reqHomework.getTitle())
                .description(reqHomework.getDescription())
                .lesson(lesson)
                .deadline(reqHomework.getDeadline())
                .deadlineEnum(reqHomework.getDeadlineEnum())
                .build();
        homeworkRepository.save(homework);
        return  ApiResponse.success(null, "Homework saved successfully");

    }

    public ApiResponse<String> deleteHomework(UUID homeworkId) {

        Homework homework = homeworkRepository.findById(homeworkId).orElseThrow(
                ()-> new DataNotFoundException("Homework not found")
        );
        homework.softDelete();
        homeworkRepository.save(homework);
        return  ApiResponse.success(null, "Homework deleted successfully");
    }


    public ApiResponse<String> updateHomework(UUID homeworkId, ReqHomework reqHomework) {

        Homework homework = homeworkRepository.findByIdAndActiveTrue(homeworkId).orElseThrow(
                ()-> new DataNotFoundException("Homework not found")
        );

        homework.setTitle(reqHomework.getTitle());
        homework.setDescription(reqHomework.getDescription());
        homework.setDeadline(reqHomework.getDeadline());
        homework.setDeadlineEnum(reqHomework.getDeadlineEnum());
        homeworkRepository.save(homework);

        return  ApiResponse.success(null, "Homework updated successfully");
    }

    public ApiResponse<ResHomework> getOneHomework(UUID homeworkId) {

        Homework homework = homeworkRepository.findByIdAndActiveTrue(homeworkId).orElseThrow(
                ()-> new DataNotFoundException("Homework not found")
        );
        return ApiResponse.success(homeworkMapper.toResHomework(homework), "Success");

    }


    public ApiResponse<ResPageable> searchHomework(String title, String deadlineEnum, String lessonTitle, int page, int size ) {
        Page<Homework> homework = homeworkRepository.searchHomework(
                title,deadlineEnum, lessonTitle, PageRequest.of(page, size));

        if (homework.getTotalElements() == 0) {
            throw new DataNotFoundException("Homework not found");
        }

        List<ResHomework> list = homework.getContent().stream().map(homeworkMapper::toResHomework).toList();
        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(homework.getTotalElements())
                .totalPage(homework.getTotalPages())
                .body(list)
                .build();
        return ApiResponse.success(resPageable, "Success");


    }






}
