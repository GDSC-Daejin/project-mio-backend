package com.gdsc.projectmiobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PostType {
    BEFORE_DEADLINE("마감전"),
    DEADLINE("마감"),
    COMPLETED("완료");

    private final String description;
}
