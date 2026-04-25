package com.example.bolalarakademiyasi.mapper;

import com.example.bolalarakademiyasi.dto.AttendanceDto;
import com.example.bolalarakademiyasi.entity.Attendance;
import org.springframework.stereotype.Component;

@Component
public class AttendanceMapper {

    public AttendanceDto toDto(Attendance attendance) {
        return AttendanceDto.builder()
                .id(attendance.getId())
                .date(attendance.getDate())
                .description(attendance.getDescription())
                .firstName(attendance.getStudent().getFirstName())
                .lastName(attendance.getStudent().getLastName())
                .status(attendance.getStatus())
                .studentId(attendance.getStudent().getId())
                .build();
    }
}
