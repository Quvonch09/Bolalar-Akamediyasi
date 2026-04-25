package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.IdList;
import com.example.bolalarakademiyasi.dto.request.ReqNotification;
import com.example.bolalarakademiyasi.dto.response.ResNotification;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.entity.Notification;
import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.NotificationMapper;
import com.example.bolalarakademiyasi.repository.NotificationRepository;
import com.example.bolalarakademiyasi.repository.StudentRepository;
import com.example.bolalarakademiyasi.repository.UserRepository;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    public ApiResponse<String> saveNotification(ReqNotification req) {

        Student student = null;
        User parent = null;
        if (req.getStudentId() != null){
            student = studentRepository.findById(req.getStudentId()).orElseThrow(() -> new DataNotFoundException("Student not found"));
        }

        if (req.getParentId() != null){
            parent = userRepository.findById(req.getParentId()).orElseThrow(() -> new DataNotFoundException("User not found"));
        }

        Notification notification = Notification.builder()
                .message(req.getMessage())
                .description(req.getDescription())
                .student(student)
                .parent(parent)
                .isRead(false)
                .build();
        notificationRepository.save(notification);
        return ApiResponse.success(null, "Success");
    }

    public ApiResponse<ResPageable> getNotifications(int page,int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Notification> notifications = notificationRepository.findAll(pageRequest);
        List<ResNotification> notificationsList = notifications.stream().map(notificationMapper::toNotificationDTO).toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(notifications.getTotalElements())
                .totalPage(notifications.getTotalPages())
                .body(notificationsList)
                .build();
        return ApiResponse.success(resPageable, "Success");

    }

    public ApiResponse<ResNotification> getNotificationById(UUID id) {
        Notification notification = notificationRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Notification not found"));
        return ApiResponse.success(notificationMapper.toNotificationDTO(notification), "Success");
    }

    public ApiResponse<String> deleteNotificationById(UUID id) {
        Notification notif = notificationRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Notification not found"));
        notificationRepository.delete(notif);
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<List<ResNotification>> getMyNotifications(CustomUserDetails customUserDetails) {
        User user = null;
        Student student = null;
        if (!customUserDetails.getRole().equals("ROLE_STUDENT")){
            user = userRepository.findByPhoneAndEnabledTrue(customUserDetails.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("User not found")
            );

            List<ResNotification> list = notificationRepository.findAllByParentId(user.getId()).stream()
                    .map(notificationMapper::toNotificationDTO).toList();
            return ApiResponse.success(list, "Success");
        } else {
            student = studentRepository.findByPhoneAndActiveTrue(customUserDetails.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Student not found")
            );

            List<ResNotification> list = notificationRepository.findAllByStudentId(student.getId()).stream()
                    .map(notificationMapper::toNotificationDTO).toList();
            return ApiResponse.success(list, "Success");
        }
    }



    public ApiResponse<String> readNotification(IdList idList){
        for (Notification notification : notificationRepository.findAllById(idList.getIdList())) {
            notification.setRead(true);
            notificationRepository.save(notification);
        }
        return ApiResponse.success(null,"success");
    }


    public ApiResponse<Long> countMyNotifications(CustomUserDetails customUserDetails) {
        User user = null;
        Student student = null;
        if (!customUserDetails.getRole().equals("ROLE_STUDENT")){
            user = userRepository.findByPhoneAndEnabledTrue(customUserDetails.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("User not found")
            );

            return ApiResponse.success(notificationRepository.countByParentIdAndReadFalse(user.getId()), "Success");
        } else {
            student = studentRepository.findByPhoneAndActiveTrue(customUserDetails.getPhone()).orElseThrow(
                    () -> new DataNotFoundException("Student not found")
            );

            return ApiResponse.success(notificationRepository.countByStudentIdAndReadFalse(student.getId()), "Success");
        }
    }


//    public ApiResponse<String> sendGroupNotification(ReqGroupNotif reqGroupNotif){
//        Group group = groupRepository.findById(reqGroupNotif.getGroupId()).orElseThrow(
//                () -> new DataNotFoundException("Group not found")
//        );
//
//        for (Student student : studentRepository.findAllByGroup_idAndActiveTrue(group.getId())) {
//            Notification notification = Notification.builder()
//                    .message(reqGroupNotif.getTitle())
//                    .description(reqGroupNotif.getDescription())
//                    .student(student)
//                    .isRead(false)
//                    .parent(null)
//                    .build();
//            notificationRepository.save(notification);
//        }
//
//        return ApiResponse.success(null, "Success");
//    }

}
