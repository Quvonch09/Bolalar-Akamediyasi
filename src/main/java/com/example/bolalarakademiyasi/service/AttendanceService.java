package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.AttendanceDto;
import com.example.bolalarakademiyasi.dto.request.ReqAttendance;
import com.example.bolalarakademiyasi.dto.request.ReqMark;
import com.example.bolalarakademiyasi.entity.Attendance;
import com.example.bolalarakademiyasi.entity.Class;
import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.enums.AttendaceEnum;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.AttendanceMapper;
import com.example.bolalarakademiyasi.repository.AttendanceRepository;
import com.example.bolalarakademiyasi.repository.ClassRepository;
import com.example.bolalarakademiyasi.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final AttendanceMapper attendanceMapper;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final MarkService markService;


    public ApiResponse<String> saveAttendance(UUID sinfId, List<ReqAttendance> reqAttendanceList){
        Class sinf = classRepository.findById(sinfId).orElseThrow(
                () -> new DataNotFoundException("Class not found")
        );

        for (ReqAttendance attendanceDto : reqAttendanceList) {
            Student student = studentRepository.findById(attendanceDto.getStudentId()).orElseThrow(
                    () -> new DataNotFoundException("Student not found")
            );

            markService.saveMark(ReqMark.builder()
                    .activityScore(attendanceDto.getActivityScore())
                    .behaviourScore(attendanceDto.getBehaviourScore())
                    .homeworkScore(attendanceDto.getHomeworkScore())
                    .studentId(student.getId())
                    .build());


            if (attendanceRepository.findByStudentIdAndDate(student.getId(), attendanceDto.getDate()) == null) {
                Attendance attendance = Attendance.builder()
                        .sinf(sinf)
                        .student(student)
                        .date(attendanceDto.getDate())
                        .status(attendanceDto.getStatus())
                        .description(attendanceDto.getStatus().equals(AttendaceEnum.KELDI) ?
                                null : attendanceDto.getDescription())
                        .build();
                attendanceRepository.save(attendance);
            } else {
                Attendance attendance =
                        attendanceRepository.findByStudentIdAndDate(student.getId(), attendanceDto.getDate());
                attendance.setStatus(attendanceDto.getStatus());
                attendance.setDescription(attendanceDto.getStatus().equals(AttendaceEnum.KELDI) ?
                        null : attendanceDto.getDescription());
                attendanceRepository.save(attendance);
            }

        }

        sendToAll(getAllAttendanceByGroup(sinf.getId()));
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<String> deleteAttendance(UUID attendanceId){
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(
                () -> new DataNotFoundException("Attendance not found")
        );
        UUID id = attendance.getSinf().getId();
        attendanceRepository.delete(attendance);

        sendToAll(getAllAttendanceByGroup(id));
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<AttendanceDto> getAttendance(UUID attendanceId){
        Attendance attendance = attendanceRepository.findById(attendanceId).orElseThrow(
                () -> new DataNotFoundException("Attendance not found")
        );

        return ApiResponse.success(attendanceMapper.toDto(attendance), "Success");
    }


    public List<AttendanceDto> getAllAttendanceByGroup(UUID groupId) {
        return attendanceRepository
                .findAllBySinfIdOrderByCreatedAtDesc(groupId)
                .stream()
                .map(attendanceMapper::toDto)
                .toList();
    }

    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(0L); // cheksiz timeout

        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    // Barcha ulangan clientlarga yuborish
    public void sendToAll(Object data) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(
                        SseEmitter.event()
                                .name("attendance")
                                .data(data)
                );
            } catch (Exception e) {
                emitters.remove(emitter);
            }
        }
    }

}