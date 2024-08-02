package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.common.Manner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MannerDto {

    private Long id;
    private Manner manner;
    private String content;
    private Long getUserId;
    private Long postUserId;
    private String createDate;
}
