package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.AttendanceDto;
import com.example.bolalarakademiyasi.dto.request.ReqAttendance;
import com.example.bolalarakademiyasi.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;

    @PostMapping
    @Operation(summary = "Guruh buyicha davomat qilish",
            description = "AttendanceEnum -> KELDI, KELMADI, SABABLI \n " +
                    "Keldi bulsa status description null buladi, aks holda desciption yoziladi")
    public ResponseEntity<ApiResponse<String>> saveAttendance(@RequestParam UUID sinfId,
                                                              @RequestBody List<ReqAttendance> reqAttendanceList) {
        return ResponseEntity.ok(attendanceService.saveAttendance(sinfId, reqAttendanceList));
    }


    @DeleteMapping("/attendanceId")
    public ResponseEntity<ApiResponse<String>> deleteAttendance(@RequestParam UUID attendanceId) {
        return ResponseEntity.ok(attendanceService.deleteAttendance(attendanceId));
    }



    @GetMapping("/{attendanceId}")
    public ResponseEntity<ApiResponse<AttendanceDto>> getAttendance(@PathVariable UUID attendanceId) {
        return ResponseEntity.ok(attendanceService.getAttendance(attendanceId));
    }


    @GetMapping("/stream/{sinfId}")
    public SseEmitter stream(@PathVariable UUID sinfId) {
        SseEmitter emitter = attendanceService.subscribe();

        try {
            // Dastlabki ma'lumotni yuboramiz
            emitter.send(
                    SseEmitter.event()
                            .name("attendance")
                            .data(attendanceService.getAllAttendanceByGroup(sinfId))
            );
        } catch (Exception e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
