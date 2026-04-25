package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.response.ResStudent;
import com.example.bolalarakademiyasi.dto.response.StudentResponse;
import com.example.bolalarakademiyasi.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StudentMapper {

    public ResStudent toStudentDTO(Student student) {

        return ResStudent.builder()
                .id(student.getId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .imgUrl(student.getImgUrl())
                .age(student.getAge() != null ? student.getAge() : 0)
                .phoneNumber(student.getPhone())
                .sinfId(student.getSinf() != null ? student.getSinf().getId() : null)
                .sinfName(student.getSinf() != null ? student.getSinf().getName() : null)
                .parentId(student.getParent() != null ? student.getParent().getId() : null)
                .parentFirstName(student.getParent() != null ? student.getParent().getFirstName() : null)
                .parentLastName(student.getParent() != null ? student.getParent().getLastName() : null)
                .parentPhone(student.getParent() != null ? student.getParent().getPhone() : null)
                .build();
    }

    public StudentResponse toResponseUser(Student user) {
        return StudentResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .age(user.getAge())
                .imgUrl(user.getImgUrl())
                .role("ROLE_STUDENT")
                .sinfId(user.getSinf() != null ? user.getSinf().getId() : null)
                .sinfName(user.getSinf() != null ? user.getSinf().getName() : null)
                .parentFirstName(user.getParent() != null ? user.getParent().getFirstName() : null)
                .parentLastName(user.getParent() != null ? user.getParent().getLastName() : null)
                .teacherFirstName(user.getSinf().getTeacher() != null ? user.getSinf().getTeacher().getFirstName() : null)
                .teacherLastName(user.getSinf().getTeacher() != null ? user.getSinf().getTeacher().getLastName() : null)
                .build();
    }
}
