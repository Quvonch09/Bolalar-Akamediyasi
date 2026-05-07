package com.example.bolalarakademiyasi.dto.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqVideoLessonPayload {
    private String title;
    private String videoUrl;
    private Integer duration;
    private Long fileSize;
    private String format;
}
