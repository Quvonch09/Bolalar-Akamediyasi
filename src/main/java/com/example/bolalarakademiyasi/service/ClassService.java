package com.example.bolalarakademiyasi.service;


import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqClass;
import com.example.bolalarakademiyasi.dto.request.ReqClassDTO;
import com.example.bolalarakademiyasi.dto.response.ClassResponse;
import com.example.bolalarakademiyasi.dto.response.ResClass;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.UserResponse;
import com.example.bolalarakademiyasi.entity.Class;
import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.entity.enums.Role;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.MarkMapper;
import com.example.bolalarakademiyasi.mapper.SinfMapper;
import com.example.bolalarakademiyasi.repository.ClassRepository;
import com.example.bolalarakademiyasi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor

public class ClassService {

    private final SinfMapper sinfMapper;
    private final ClassRepository classRepository;
    private final UserRepository userRepository;


    public ApiResponse<String> saveClass(ReqClass reqClass) {

        User teacher = userRepository.findById(reqClass.getTeacherId()).orElseThrow(
                () -> new DataNotFoundException("Teacher not found"));

        Class newClass = Class.builder()
                .name(reqClass.getName())
                .shiftEnum(reqClass.getShift())
                .teacher(teacher)
                .build();

        classRepository.save(newClass);
        return ApiResponse.success(null, "Class saved successfully");
    }


    public ApiResponse<String> deleteClass(UUID classId) {

        Class existingClass = classRepository.findByIdAndActiveTrue(classId)
                .orElseThrow(() -> new DataNotFoundException("Class not found!"));

        existingClass.softDelete();
        classRepository.save(existingClass);
        return ApiResponse.success(null, "Class deleted successfully");
    }


    public ApiResponse<ResClass> getOneClass(UUID classId) {

        Class foundClass = classRepository.findByIdAndActiveTrue(classId)
                .orElseThrow(() -> new DataNotFoundException("Class not found!"));
        return ApiResponse.success(sinfMapper.toFullDTO(foundClass), "Success");

    }

    public ApiResponse<List<ResClass>> getAllClasses() {
        List<ResClass> classes = classRepository.findAll().stream().map(sinfMapper::toFullDTO).toList();

        return ApiResponse.success(classes, "Success");
    }


    public ApiResponse<String> updateClass(ReqClassDTO reqClassDTO) {
        Class updatedClass = classRepository.findById(reqClassDTO.getId()).orElseThrow(
                () -> new DataNotFoundException("Class not found!")
        );

        User teacher = userRepository.findById(reqClassDTO.getTeacherId()).orElseThrow(
                () -> new DataNotFoundException("Teacher not found!")
        );

        updatedClass.setName(reqClassDTO.getName());
        updatedClass.setShiftEnum(reqClassDTO.getShiftEnum());
        updatedClass.setTeacher(teacher);

        classRepository.save(updatedClass);
        return ApiResponse.success(null, "Class updated successfully");
    }



    public ApiResponse<ResPageable> searchCLass(String name, String teacherName, int page, int size){
        Page<Class> classes = classRepository.searchClass(name, teacherName, PageRequest.of(page, size));
        if (classes.getTotalElements() == 0){
            throw new DataNotFoundException("Class not found!");
        }

        List<ResClass> list = classes.getContent().stream().map(sinfMapper::toFullDTO).toList();
        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(classes.getTotalElements())
                .totalPage(classes.getTotalPages())
                .body(list)
                .build();
        return ApiResponse.success(resPageable, "Success");
    }



}
