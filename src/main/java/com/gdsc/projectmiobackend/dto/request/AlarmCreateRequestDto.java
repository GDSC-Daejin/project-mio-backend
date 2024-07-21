package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.entity.Alarm;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlarmCreateRequestDto {

    private String content;

    private Long postId;

    private Long userId;

    public Alarm toEntity(Post post, UserEntity userEntity){
        return Alarm.builder()
                .createDate(LocalDateTime.now())
                .content(content)
                .post(post)
                .userEntity(userEntity)
                .build();
    }
}
