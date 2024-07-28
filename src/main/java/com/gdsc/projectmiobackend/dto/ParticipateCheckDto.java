package com.gdsc.projectmiobackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
public class ParticipateCheckDto {
    private Boolean check;
}
