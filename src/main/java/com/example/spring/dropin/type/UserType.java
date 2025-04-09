package com.example.spring.dropin.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserType {
    ROLE_USER("회원"),
    ROLE_COACH("코치"),
    ROLE_ADMIN("운영진"),
    ROLE_MASTER("마스터");

    private final String description;
}
