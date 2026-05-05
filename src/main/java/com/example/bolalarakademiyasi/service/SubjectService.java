package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqSubject;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResSubject;
import com.example.bolalarakademiyasi.entity.Lesson;
import com.example.bolalarakademiyasi.entity.Subject;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.SubjectMapper;
import com.example.bolalarakademiyasi.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.bv.time.past.AbstractPastInstantBasedValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;


    public ApiResponse<String> saveSubject(ReqSubject reqSubject){
        Subject subject = Subject.builder()
                .name(reqSubject.getName())
                .description(reqSubject.getDescription())
                .build();
        subjectRepository.save(subject);
        return ApiResponse.success(null, "Subject saved successfully");

    }


    public ApiResponse<String> deleteSubject(UUID subjectId){
        Subject subject = subjectRepository.findById(subjectId).orElseThrow( () -> new DataNotFoundException("subject not found"));


        subject.softDelete();
        subjectRepository.save(subject);
        return ApiResponse.success(null, "Subject deleted successfully");
    }



    public  ApiResponse<ResSubject> getOneSubject(UUID subjectId){
        Subject subject = subjectRepository.findById(subjectId).orElseThrow( () -> new DataNotFoundException("subject not found"));

        return ApiResponse.success(subjectMapper.toResSubject(subject), "Success");
    }



    public ApiResponse<String>  updateSubject(UUID subjectId, ReqSubject reqSubject){
        Subject subject = subjectRepository.findById(subjectId).orElseThrow(
                () -> new DataNotFoundException("Subject not found")
        );
        subject.setName(reqSubject.getName());
        subject.setDescription(reqSubject.getDescription());
        subjectRepository.save(subject);
        return ApiResponse.success(null, "Subject updated successfully");
    }

    public ApiResponse<ResPageable> searchSubject(String name, String description, int page, int size) {
        Page<Subject> subjectPage = subjectRepository.searchSubject(
                name, description, PageRequest.of(page, size)
        );
        if (subjectPage.getTotalElements() == 0) {
            throw new DataNotFoundException("Subject not found");
        }

        List<ResSubject> list = subjectPage.getContent()
                .stream()
                .map(subjectMapper::toResSubject)
                .toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(subjectPage.getTotalElements())
                .totalPage(subjectPage.getTotalPages())
                .body(list)
                .build();
        return ApiResponse.success(resPageable, "Success");
    }






}
