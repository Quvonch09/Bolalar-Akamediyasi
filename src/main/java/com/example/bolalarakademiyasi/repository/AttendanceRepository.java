package com.example.bolalarakademiyasi.repository;

import com.example.bolalarakademiyasi.entity.Attendance;
import com.example.bolalarakademiyasi.entity.enums.AttendaceEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, UUID> {
    Attendance findByStudentIdAndDate(UUID studentId, LocalDate date);

    List<Attendance> findAllBySinfIdOrderByCreatedAtDesc(UUID sinfId);

    List<Attendance> findAllByStudentIdAndDateBetween(UUID studentId, LocalDate start, LocalDate end);

    Integer countByStudentId(UUID studentId);

    Integer countByStudentIdAndStatus(UUID studentId, AttendaceEnum status);
}
