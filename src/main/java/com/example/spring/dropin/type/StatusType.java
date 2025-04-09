package com.example.spring.dropin.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StatusType {
    APPROVED("승인"),
    WITHDRAWN("탈퇴"),
    PENDING("대기");

    private final String description;
}
