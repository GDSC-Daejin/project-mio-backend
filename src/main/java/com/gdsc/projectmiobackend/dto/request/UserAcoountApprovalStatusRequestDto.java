package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.common.AccountApprovalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAcoountApprovalStatusRequestDto {

    @Schema(description = "계좌 저장 승인 상태.")
    @NotEmpty(message="계좌 저장 승인 상태는 필수입니다.")
    private AccountApprovalStatus status;
}
