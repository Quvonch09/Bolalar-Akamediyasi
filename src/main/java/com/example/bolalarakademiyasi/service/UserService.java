package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.*;
import com.example.bolalarakademiyasi.dto.response.*;
import com.example.bolalarakademiyasi.entity.Attendance;
import com.example.bolalarakademiyasi.entity.Mark;
import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.entity.enums.*;
import com.example.bolalarakademiyasi.exception.BadRequestException;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.SinfMapper;
import com.example.bolalarakademiyasi.mapper.StudentMapper;
import com.example.bolalarakademiyasi.mapper.UserMapper;
import com.example.bolalarakademiyasi.repository.*;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import com.example.bolalarakademiyasi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper mapper;
    private final JwtService jwtService;
    private final ClassRepository classRepository;
    private final StudentRepository studentRepository;
    private final SinfMapper sinfMapper;
    private final StudentMapper studentMapper;
    private final MarkRepository markRepository;
    private final AttendanceRepository attendanceRepository;
    private final AuthService authService;

    public ApiResponse<?> getProfile(CustomUserDetails currentUser) {
        if (currentUser.getRole().equals("ROLE_STUDENT")) {
            Student student = currentUser.getStudent();
            return ApiResponse.success(studentMapper.toResponseUser(student), "Success");
        } else {
            User user = currentUser.getUser();
            return ApiResponse.success(mapper.toResponseUser(user), "success");
        }
    }

    public ApiResponse<String> update(CustomUserDetails current, UserDTO req) {

        User currentUser = current.getUser();
        UUID targetId = req.getId();

        User targetUser;

        if (targetId == null) {
            targetUser = currentUser;
        } else {
            if (targetId.equals(currentUser.getId())) {
                targetUser = currentUser;
            } else {
//                if (!currentUser.getRole().name().equals("ROLE_ADMIN") &&
//                        !currentUser.getRole().name().equals("ROLE_SUPER_ADMIN")) {
//                    return ApiResponse.error("Siz boshqa userni update qila olmaysiz!");
//                }
                targetUser = userRepository.findById(targetId)
                        .orElseThrow(() -> new DataNotFoundException("User topilmadi"));
            }
        }
        String oldPhone = targetUser.getPhone();
        String newPhone = req.getPhone();

        if (req.getFirstName() != null)
            targetUser.setFirstName(req.getFirstName());

        if (req.getLastName() != null)
            targetUser.setLastName(req.getLastName());

        if (req.getImageUrl() != null)
            targetUser.setImgUrl(req.getImageUrl());

        String newToken = null;

        if (newPhone != null && !newPhone.equals(oldPhone)) {
            targetUser.setPhone(newPhone);
            newToken = jwtService.generateToken(newPhone, targetUser.getRole().name());
        }

        userRepository.save(targetUser);

        return ApiResponse.success(newToken, "success");

    }

    public ApiResponse<String> deleteById(UUID userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DataNotFoundException("user not found"));
        if (user.getRole().name().equals("ROLE_TEACHER")) {
            int size = classRepository.findAllByTeacherIdAndActiveTrue(user.getId()).size();
            if (size == 0) {
                user.setActive(false);
            } else {
                throw new BadRequestException("Cannot delete teacher");
            }
        } else if (user.getRole().name().equals("ROLE_PARENT")) {
            int size = studentRepository.findAllByParent_IdAndActiveTrue(user.getId()).size();
            if (size == 0) {
                user.setActive(false);
            } else {
                throw new BadRequestException("Cannot delete parent");
            }
        } else {
            user.setActive(false);
        }

        userRepository.save(user);
        return ApiResponse.success(null, "success");
    }

    public ApiResponse<UserResponse> getOneUser(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new DataNotFoundException("User topilmadi"));
        return ApiResponse.success(mapper.toResponseUser(user), "Success");
    }


    public ApiResponse<ResPageable> getAllUsersSearch(String name, String phone, Role role, int page, int size) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by("id").descending());

        Page<User> users = userRepository.searchUser(name, phone, role != null ? role.name():null, pageable);

        List<UserResponse> list = users.stream()
                .map(mapper::toResponseUser)
                .toList();

        if (users.isEmpty()) {
            return ApiResponse.error("Foydalanuvchilar topilmadi");
        }

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(users.getTotalElements())
                .totalPage(users.getTotalPages())
                .body(list)
                .build();

        return ApiResponse.success(resPageable, "success");
    }


    public ApiResponse<ResTeacher> getOneTeacher(UUID id) {
        User teacher = userRepository.findByIdAndEnabledTrue(id).orElseThrow(
                () -> new DataNotFoundException("Teacher not found")
        );

        List<ResStudent> studentList = studentRepository.findAllByTeacher(teacher.getId())
                .stream().map(studentMapper::toStudentDTO).toList();
        List<ResSinfDTO> groupList = classRepository.findAllByTeacherIdAndActiveTrue(teacher.getId())
                .stream().map(sinfMapper::toRes).toList();

        return ApiResponse.success(mapper.resTeacher(teacher, studentList, groupList), "Success");
    }


    public ApiResponse<List<UserResponse>> getAllList(Role role) {
        List<UserResponse> list = userRepository.findAllByRole(role).stream().map(mapper::toResponseUser).toList();
        return ApiResponse.success(list, "success");
    }


    public ApiResponse<StudentStatsDTO> getStats(CustomUserDetails cud, UUID studentId) {

        User parent = userRepository.findByPhoneAndEnabledTrue(cud.getPhone())
                .orElseThrow(() -> new DataNotFoundException("Parent not found"));

        Student student = getStudentForParent(studentId, parent);

        Double avg = markRepository.avgTotalScore(student.getId());
        if (avg == null) avg = 0.0;

        int groupsCount = (student.getSinf() != null) ? 1 : 0;

        // Attendance hisoblash
        Integer totalDays = attendanceRepository.countByStudentId(student.getId());
        Integer presentDays = attendanceRepository.countByStudentIdAndStatus(student.getId(),AttendaceEnum.KELDI);

        if (totalDays == null) totalDays = 0;
        if (presentDays == null) presentDays = 0;

        int attendancePercent = 0;

        if (totalDays > 0) {
            attendancePercent = (presentDays * 100) / totalDays;
        }

        StudentStatsDTO dto = StudentStatsDTO.builder()
                .averageGrade(round1(avg))
                .attendancePercent(attendancePercent)
                .subjectsCount(groupsCount)
                .build();


        return ApiResponse.success(dto, "Success");
    }


    public ApiResponse<List<WeekMarkDTO>> getMarks(CustomUserDetails cud,
                                                   UUID studentId,
                                                   PeriodFilter filter) {

        User parent = userRepository.findByPhoneAndEnabledTrue(cud.getPhone())
                .orElseThrow(() -> new DataNotFoundException("Parent not found"));

        Student student = getStudentForParent(studentId, parent);

        LocalDate base = LocalDate.now(); // date yo'q, doim bugungi sana

        LocalDate start;
        LocalDate end;

        if (filter == PeriodFilter.WEEKLY) {
            start = base.with(java.time.DayOfWeek.MONDAY);
            end = start.plusDays(5); // 6 kun: Mon..Sat
        } else { // MONTHLY
            start = base.withDayOfMonth(1);
            end = base.withDayOfMonth(base.lengthOfMonth());
        }

        List<Mark> marks = markRepository
                .findAllByStudentIdAndActiveTrueAndDateBetweenOrderByDateAsc(
                        student.getId(), start, end
                );

        List<WeekMarkDTO> res = marks.stream()
                .map(m -> WeekMarkDTO.builder()
                        .day(WeekDays.valueOf(m.getDate().getDayOfWeek().name()))
                        .score(m.getTotalScore())
                        .category(m.getMarkCategoryStatus())
                        .date(m.getDate())
                        .build())
                .toList();

        return ApiResponse.success(res, "Success");
    }


    public ApiResponse<List<WeekAttendanceDTO>> getAttendance(CustomUserDetails cud,
                                                              UUID studentId,
                                                              AttendancePeriodFilter filter) {

        User parent = userRepository.findByPhoneAndEnabledTrue(cud.getPhone())
                .orElseThrow(() -> new DataNotFoundException("Parent not found"));

        Student student = getStudentForParent(studentId, parent);

        LocalDate base = LocalDate.now(); // date yo'q, doim bugungi sana

        LocalDate start;
        LocalDate end;

        if (filter == AttendancePeriodFilter.WEEKLY) {
            start = base.with(java.time.DayOfWeek.MONDAY);
            end = start.plusDays(5); // 6 kun: Mon..Sat
        } else { // MONTHLY
            start = base.withDayOfMonth(1);
            end = base.withDayOfMonth(base.lengthOfMonth()); // oy oxiri
        }

        List<Attendance> list =
                attendanceRepository.findAllByStudentIdAndDateBetween(student.getId(), start, end);

        // date -> present (KELDI=true)
        Map<LocalDate, Boolean> map = new HashMap<>();
        for (Attendance a : list) {
            map.put(a.getDate(), a.getStatus() == AttendaceEnum.KELDI);
        }

        List<WeekAttendanceDTO> res = new ArrayList<>();

        if (filter == AttendancePeriodFilter.WEEKLY) {
            // doim 6 ta chip qaytaradi
            for (int i = 0; i < 6; i++) {
                LocalDate d = start.plusDays(i);
                res.add(WeekAttendanceDTO.builder()
                        .day(WeekDays.valueOf(d.getDayOfWeek().name()))
                        .present(map.getOrDefault(d, false))
                        .build());
            }
        } else {
            // oylik: faqat bor kunlar (attendance olingan kunlar) qaytariladi
            for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
                if (map.containsKey(d)) {
                    res.add(WeekAttendanceDTO.builder()
                            .day(WeekDays.valueOf(d.getDayOfWeek().name()))
                            .present(map.get(d))
                            .build());
                }
            }
        }

        return ApiResponse.success(res, "Success");
    }


    private Student getStudentForParent(UUID studentId, User parent) {
        Student s = studentRepository.findById(studentId)
                .orElseThrow(() -> new DataNotFoundException("Student not found"));

        if (s.getParent() == null || !Objects.equals(s.getParent().getId(), parent.getId())) {
            throw new RuntimeException("Bu farzand sizga tegishli emas");
        }
        return s;
    }

    private double round1(double v) {
        return Math.round(v * 10.0) / 10.0;
    }



    public ApiResponse<List<ResStudent>> getAllStudentsByParent(CustomUserDetails customUserDetails) {
        if (!customUserDetails.getRole().equals(Role.ROLE_PARENT.name())) {
            return ApiResponse.error("Only PARENT");
        } else {
            List<ResStudent> list = studentRepository.findAllByParent_IdAndActiveTrue(
                    customUserDetails.getUser().getId()).stream().map(studentMapper::toStudentDTO).toList();
            return ApiResponse.success(list, "Success");
        }
    }



//    @Transactional
//    public ApiResponse<String> approveStudent(UUID studentId) {
//
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new DataNotFoundException("Student not found"));
//
//        student.setActive(true);
//        studentRepository.save(student);
//
//        User parent = student.getParent(); // null bo'lmasligi kerak
//        parent.setActive(true);
//        userRepository.save(parent);
//
//        // ✅ login/parol tayyorlash
//        String studentPhone = student.getPhone();
//        String studentPass  = authService.last4(studentPhone);
//
//        String parentPhone = parent.getPhone();
//        String parentPass  = authService.last4(parentPhone);
//
//        return ApiResponse.success(null, "Approved and notified");
//    }
}
