package com.gdsc.projectmiobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountApprovalStatus {
    PENDING("대기중"),   // 대기중 상태
    APPROVED("승인됨"),  // 승인된 상태
    REJECTED("거부됨");  // 거부된 상태

    private final String accountApprovalStatus;
}
