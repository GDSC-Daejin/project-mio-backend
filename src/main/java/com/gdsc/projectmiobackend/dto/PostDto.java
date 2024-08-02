package com.gdsc.projectmiobackend.dto;


import com.gdsc.projectmiobackend.common.PostType;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDate targetDate;
    private LocalTime targetTime;
    private Category category;
    private Boolean verifyGoReturn;
    private Integer numberOfPassengers;
    private UserEntity user;
    private Long viewCount;
    private List<UserEntity> participants;
    private Double latitude;
    private Double longitude;
    private Long bookMarkCount;
    private Long participantsCount;
    private String location;
    private Long cost;
    private String isDeleteYN;
    private PostType postType;
}
