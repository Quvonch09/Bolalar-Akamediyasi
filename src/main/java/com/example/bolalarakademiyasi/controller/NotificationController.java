package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.IdList;
import com.example.bolalarakademiyasi.dto.request.ReqNotification;
import com.example.bolalarakademiyasi.dto.response.ResNotification;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import com.example.bolalarakademiyasi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<ApiResponse<String>> createNotification(@RequestBody ReqNotification req) {
        return ResponseEntity.ok(notificationService.saveNotification(req));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<ResPageable>> getNotifications(@RequestParam(defaultValue = "0") int page ,
                                                                     @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(notificationService.getNotifications(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResNotification>> getNotificationById(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(notificationService.deleteNotificationById(id));
    }


    @GetMapping("/my")
    public ResponseEntity<ApiResponse<List<ResNotification>>> getMyNotifications(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(notificationService.getMyNotifications(customUserDetails));
    }


    @PutMapping("/read")
    public ResponseEntity<ApiResponse<String>> readNotification(@RequestBody IdList idList) {
        return ResponseEntity.ok(notificationService.readNotification(idList));
    }


    @GetMapping("/count")
    public ResponseEntity<ApiResponse<Long>> countNotification(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return ResponseEntity.ok(notificationService.countMyNotifications(customUserDetails));
    }

//    @PostMapping("/withGroup")
//    public ResponseEntity<ApiResponse<String>> createNotificationWithGroup(@RequestBody ReqGroupNotif reqGroupNotif) {
//        return ResponseEntity.ok(notificationService.sendGroupNotification(reqGroupNotif));
//    }

}
