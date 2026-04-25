package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.SinfDTO;
import com.example.bolalarakademiyasi.dto.response.ResClass;
import com.example.bolalarakademiyasi.dto.response.ResSinfDTO;
import com.example.bolalarakademiyasi.entity.Class;
import com.example.bolalarakademiyasi.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SinfMapper {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public SinfDTO sinfDto(Class sinf) {
        return SinfDTO.builder()
                .id(sinf.getId())
                .name(sinf.getName())
                .teacherFirstName(sinf.getTeacher() != null ? sinf.getTeacher().getFirstName():null)
                .teacherLastName(sinf.getTeacher() != null ? sinf.getTeacher().getLastName():null)
                .teacherId(sinf.getTeacher() != null ? sinf.getTeacher().getId() : null)
                .students(studentRepository.findAllBySinf_idAndActiveTrue(sinf.getId()).stream()
                        .map(studentMapper::toStudentDTO).toList())
                .build();
    }


    public ResSinfDTO toRes(Class group) {
        return ResSinfDTO.builder()
                .id(group.getId())
                .name(group.getName())
                .teacherFirstName(group.getTeacher() != null ? group.getTeacher().getFirstName() : null)
                .teacherLastName(group.getTeacher() != null ? group.getTeacher().getLastName() : null)
                .teacherId(group.getTeacher() != null ? group.getTeacher().getId() : null)
                .build();
    }


    public ResClass toDtoRes(Class sinf) {
        return ResClass.builder()
                .id(sinf.getId())
                .name(sinf.getName())
                .teacherFirstName(sinf.getTeacher() != null ? sinf.getTeacher().getFirstName() : null)
                .teacherLastName(sinf.getTeacher() != null ? sinf.getTeacher().getLastName() : null)
                .studentCount(studentRepository.countBySinf_id(sinf.getId()))
                .build();
    }

    public ResClass toFullDTO (Class sinf) {
            return  ResClass.builder()
                    .id(sinf.getId())
                    .name(sinf.getName())
                    .teacherFirstName(sinf.getTeacher() != null ? sinf.getTeacher().getFirstName() : null)
                    .teacherLastName(sinf.getTeacher() != null ? sinf.getTeacher().getLastName() : null)
                    .studentCount(studentRepository.countBySinf_id(sinf.getId()))
                    .build();
    }
}
