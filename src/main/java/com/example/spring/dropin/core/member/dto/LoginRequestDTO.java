package com.example.spring.dropin.core.member.dto;

import com.example.spring.dropin.domain.Member;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LoginRequestDTO {
    private String userId;
    private String password;

    public Member toMember() {
        return Member.builder()
                .userId(userId)
                .password(password)
                .build();
    }
}
