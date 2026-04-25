package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.response.*;
import com.example.bolalarakademiyasi.entity.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public UserResponse  toResponseUser(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .phone(user.getPhone())
            .imgUrl(user.getImgUrl())
            .role(user.getRole().name())
                .lastName(user.getLastName())
            .build();
    }

//    public UserDTO toUserDTO(User user) {
//    return UserDTO.builder()
//            .id(user.getId())
//            .fullName(user.getFullName())
//            .phone(user.getPhone())
//            .imageUrl(user.getImageUrl())
//            .build();
//    }

    public ResTeacher resTeacher(User teacher, List<ResStudent> studentList, List<ResSinfDTO> sinfDTOList) {
        return ResTeacher.builder()
                .id(teacher.getId())
                .firstName(teacher.getFirstName())
                .lastName(teacher.getLastName())
                .phone(teacher.getPhone())
                .imageUrl(teacher.getImgUrl())
                .studentList(studentList)
                .sinfDTOList(sinfDTOList)
                .build();
    }
}
