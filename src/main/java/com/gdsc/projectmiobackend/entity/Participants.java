package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import com.gdsc.projectmiobackend.dto.ParticipateDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ApprovalOrReject approvalOrReject;

    @Nullable
    private Boolean verifyFinish;

    @Nullable
    private Boolean driverMannerFinish;

    @Nullable
    private Boolean passengerMannerFinish;

    @Nullable
    private String content;

    @Nullable
    private Long postUserId;

    private String isDeleteYN;

    public ParticipateDto toDto() {
        return ParticipateDto.builder()
                .postId(post.getId())
                .userId(user.getId())
                .content(content)
                .participantId(id)
                .approvalOrReject(approvalOrReject)
                .driverMannerFinish(driverMannerFinish)
                .passengerMannerFinish(passengerMannerFinish)
                .postUserId(postUserId)
                .verifyFinish(verifyFinish)
                .isDeleteYN(isDeleteYN)
                .build();
    }
}
