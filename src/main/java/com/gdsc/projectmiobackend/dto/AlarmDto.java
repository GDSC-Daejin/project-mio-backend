package com.gdsc.projectmiobackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmDto {
    private Long id;
    private LocalDateTime createDate;
    private String content;
    private Long postId;
    private Long userId;
}
