package com.example.bolalarakademiyasi.service;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqFeedback;
import com.example.bolalarakademiyasi.dto.response.ResFeedback;
import com.example.bolalarakademiyasi.dto.response.ResFeedbackDTO;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.entity.Feedback;
import com.example.bolalarakademiyasi.entity.User;
import com.example.bolalarakademiyasi.exception.DataNotFoundException;
import com.example.bolalarakademiyasi.mapper.FeedbackMapper;
import com.example.bolalarakademiyasi.repository.FeedbackRepository;
import com.example.bolalarakademiyasi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper  feedbackMapper;


    public ApiResponse<String> saveFeedback(ReqFeedback reqFeedback) {

        User teacher = userRepository.findById(reqFeedback.getTeacherId())
                .orElseThrow(() -> new DataNotFoundException("Teacher not found!"));

        Feedback feedback = Feedback.builder()
                .title(reqFeedback.getTitle())
                .rating(reqFeedback.getRating())
                .teacher(teacher)
                .build();

        feedbackRepository.save(feedback);
        return ApiResponse.success(null, "Feedback saved successfully");
    }


    public ApiResponse<String> deleteFeedback(UUID feedbackId) {

        Feedback feedback = feedbackRepository.findByIdAndActiveTrue(feedbackId)
                .orElseThrow(() -> new DataNotFoundException("Feedback not found!"));

        feedback.softDelete();
        feedbackRepository.save(feedback);
        return ApiResponse.success(null, "Feedback deleted successfully");
    }



    public ApiResponse<ResFeedback> getOneFeedback(UUID feedbackId) {

        Feedback feedback = feedbackRepository.findByIdAndActiveTrue(feedbackId)
                .orElseThrow(() -> new DataNotFoundException("Feedback not found!"));

        return ApiResponse.success(feedbackMapper.toResponse(feedback), "Success");

    }



    public ApiResponse<String> updateFeedback(UUID feedbackId, ReqFeedback reqFeedback) {

        Feedback feedback = feedbackRepository.findByIdAndActiveTrue(feedbackId)
                .orElseThrow(() -> new DataNotFoundException("Feedback not found!"));

        User teacher = userRepository.findById(reqFeedback.getTeacherId())
                .orElseThrow(() -> new DataNotFoundException("Teacher not found!"));

        feedback.setTitle(reqFeedback.getTitle());
        feedback.setRating(reqFeedback.getRating());
        feedback.setTeacher(teacher);

        feedbackRepository.save(feedback);
        return ApiResponse.success(null, "Feedback updated successfully");
    }



    public ApiResponse<ResPageable> searchFeedback(String title, String teacherName, int page, int size) {

        Page<Feedback> feedbackPage = feedbackRepository.searchFeedback(
                title, teacherName, PageRequest.of(page, size));

        if (feedbackPage.getTotalElements() == 0) {
            throw new DataNotFoundException("Feedback not found!");
        }

        List<ResFeedback> list = feedbackPage.getContent()
                .stream()
                .map(feedbackMapper::toResponse)
                .toList();

        ResPageable resPageable = ResPageable.builder()
                .page(page)
                .size(size)
                .totalElements(feedbackPage.getTotalElements())
                .totalPage(feedbackPage.getTotalPages())
                .body(list)
                .build();

        return ApiResponse.success(resPageable, "Success");
    }

}