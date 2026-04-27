package com.example.bolalarakademiyasi.controller;

import com.example.bolalarakademiyasi.dto.ApiResponse;
import com.example.bolalarakademiyasi.dto.request.ReqFeedback;
import com.example.bolalarakademiyasi.dto.response.ResClass;
import com.example.bolalarakademiyasi.dto.response.ResFeedback;
import com.example.bolalarakademiyasi.dto.response.ResPageable;
import com.example.bolalarakademiyasi.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/feedbacks")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    @Operation(description = "rating = 1, 2, 3, 4, 5 ")
    public ResponseEntity<ApiResponse<String>> save(@RequestBody ReqFeedback reqFeedback) {
        return ResponseEntity.ok(feedbackService.saveFeedback(reqFeedback));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(feedbackService.deleteFeedback(id));
    }


    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResFeedback>> getOne(@PathVariable UUID id) {
        return ResponseEntity.ok(feedbackService.getOneFeedback(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> update(
            @PathVariable UUID id,
            @Valid @RequestBody ReqFeedback reqFeedback) {
        return ResponseEntity.ok(feedbackService.updateFeedback(id, reqFeedback));
    }


    @GetMapping("/search")
    public ResponseEntity<ApiResponse<ResPageable>> search(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String teacherName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(feedbackService.searchFeedback(title, teacherName, page, size));
    }
}