package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.StudentDTO;
import com.example.bolalarakademiyasi.dto.TopStudentDto;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.dto.response.ResStudent;
import com.example.bolalarakademiyasi.entity.Class;
import com.example.bolalarakademiyasi.entity.Student;
import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.entity.enums.Role;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.StudentMapper;
import com.example.bolalarakademiyasi.repository.ClassRepository;
import com.example.bolalarakademiyasi.repository.MarkRepository;
import com.example.bolalarakademiyasi.repository.StudentRepository;
import com.example.bolalarakademiyasi.repository.UserRepository;
import com.example.bolalarakademiyasi.security.CustomUserDetails;
import com.example.bolalarakademiyasi.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final JwtService jwtService;
    private final ClassRepository sinfRepository;
    private final UserRepository userRepository;
    private final MarkRepository markRepository;

    public ApiResponse<ResPageable> getStudents(String name,String phone,int page, int size) {

        PageRequest pageRequest = PageRequest.of(page, size,Sort.by("id").descending());
        Page<Student> students = studentRepository.searchStudents(name,phone,pageRequest);

        List<ResStudent> list = students.stream().map(studentMapper::toStudentDTO).toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(students.getTotalElements())
                .totalPage(students.getTotalPages())
                .body(list)
                .build();
        return ApiResponse.success(resPageable, "Success");
    }


    public ApiResponse<List<ResStudent>> getStudentList(){
        List<ResStudent> list = studentRepository.findAllByActiveTrue().stream().map(studentMapper::toStudentDTO).toList();
        return ApiResponse.success(list, "Success");
    }


    public ApiResponse<ResStudent> getById(UUID id) {

        Student student = studentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Student not found"));
        return ApiResponse.success(studentMapper.toStudentDTO(student), "Success");
    }

    public ApiResponse<String> delete(UUID id) {
        Student student = studentRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Student not found"));
        student.setActive(false);
        studentRepository.save(student);
        return ApiResponse.success(null, "Success");
    }

    public ApiResponse<String> update(CustomUserDetails current, StudentDTO req) {

        boolean isAdmin = "ROLE_ADMIN".equals(current.getRole()) || "ROLE_SUPER_ADMIN".equals(current.getRole());

        Student targetStudent;

        if (req.getId() == null) {
            targetStudent = studentRepository.findByPhoneAndActiveTrue(current.getUsername())
                    .orElseThrow(() -> new DataNotFoundException("Student topilmadi"));
        } else {
            if (!isAdmin) {
                return ApiResponse.error("Siz boshqa studentni update qila olmaysiz!");
            }

            targetStudent = studentRepository.findById(req.getId())
                    .orElseThrow(() -> new DataNotFoundException("Student topilmadi: "));
        }

        String oldPhone = targetStudent.getPhone();

        if (req.getPhone() != null) targetStudent.setPhone(req.getPhone());
        if (req.getFirstName() != null) targetStudent.setFirstName(req.getFirstName());
        if (req.getLastName() != null) targetStudent.setLastName(req.getLastName());
        if (req.getImgUrl() != null) targetStudent.setImgUrl(req.getImgUrl());
        User parent = userRepository.findByPhoneAndEnabledTrue(req.getParentPhone()).orElseThrow(
                () -> new DataNotFoundException("Parent not founda")
        );
        targetStudent.setParent(parent);

        Student saved = studentRepository.save(targetStudent);

        String token = null;

        if (req.getPhone() != null && !req.getPhone().equals(oldPhone)) {
            CustomUserDetails userDetails = CustomUserDetails.fromStudent(saved);
            token = jwtService.generateToken(
                    userDetails.getUsername(),
                    userDetails.getRole()
            );
        }

        return ApiResponse.success(token, "Success");
    }


    public ApiResponse<String> updateGroup(UUID studentId, UUID sinfId){
        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new DataNotFoundException("Student not found")
        );

        Class sinf = sinfRepository.findByIdAndActiveTrue(sinfId).orElseThrow(
                () -> new DataNotFoundException("Group id not found")
        );

        student.setSinf(sinf);
        studentRepository.save(student);
        return ApiResponse.success(null, "Success");
    }


    public ApiResponse<List<TopStudentDto>> getTop5Students(UserDetails principal) {

        String phone = principal.getUsername();

        Optional<User> optUser = userRepository.findByPhoneAndEnabledTrue(phone);

        List<Object[]> rows;

        if (optUser.isPresent()) {
            User u = optUser.get();

            if (u.getRole() == Role.ROLE_TEACHER) {
                List<UUID> groupIds = sinfRepository.findIdsByTeacherId(u.getId());

                if (groupIds == null || groupIds.isEmpty()) {
                    return ApiResponse.success(List.of(), "Teacherga group biriktirilmagan");
                }

                rows = markRepository.topStudentsByAvgForGroups(groupIds, PageRequest.of(0, 5));
            } else {
                rows = markRepository.topAllStudentsByAvg(PageRequest.of(0, 5));
            }

        } else {

            boolean isStudent = studentRepository.findByPhoneAndActiveTrue(phone).isPresent();

            if (!isStudent) {
                return ApiResponse.error("Unauthorized");
            }

            rows = markRepository.topAllStudentsByAvg(PageRequest.of(0, 5));
        }

        List<TopStudentDto> topStudents = rows.stream()
                .map(r -> {
                    return TopStudentDto.builder()
                            .studentId((UUID) r[0])
                            .studentFirstName((String) r[1])
                            .studentLastName((String) r[2])
                            .totalScore(((Number) r[3]).intValue())
                            .imageUrl((String) r[4])
                            .build();
                })
                .toList();

        return ApiResponse.success(topStudents, "Success");
    }


}
