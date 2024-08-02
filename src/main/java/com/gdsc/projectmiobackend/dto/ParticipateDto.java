package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ParticipateDto {
    private Long participantId;
    private Long postId;
    private Long userId;
    private Long postUserId;
    private String content;
    private ApprovalOrReject approvalOrReject;
    private Boolean driverMannerFinish;
    private Boolean passengerMannerFinish;
    private Boolean verifyFinish;
    private String isDeleteYN;

    public ParticipateDto(Long participantId, Long postId, Long userId, Long postUserId, String content, ApprovalOrReject approvalOrReject, Boolean driverMannerFinish, Boolean passengerMannerFinish, Boolean verifyFinish, String isDeleteYN) {
        this.participantId = participantId;
        this.postId = postId;
        this.userId = userId;
        this.postUserId = postUserId;
        this.content = content;
        this.approvalOrReject = approvalOrReject;
        this.driverMannerFinish = driverMannerFinish;
        this.passengerMannerFinish = passengerMannerFinish;
        this.verifyFinish = verifyFinish;
        this.isDeleteYN = isDeleteYN;
    }
}
