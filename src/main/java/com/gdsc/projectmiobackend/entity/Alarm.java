package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.dto.AlarmDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createDate;

    private String content;

    @ManyToOne(fetch = FetchType.EAGER)
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    private UserEntity userEntity;

    public Alarm(LocalDateTime createDate, String content, Post post, UserEntity userEntity) {
        this.createDate = createDate;
        this.content = content;
        this.post = post;
        this.userEntity = userEntity;
    }

    public AlarmDto toDto() {
        return AlarmDto.builder()
                .id(id)
                .content(content)
                .createDate(createDate)
                .postId(post != null ? post.getId() : null)
                .userId(userEntity != null ? userEntity.getId() : null)
                .build();
    }
}
