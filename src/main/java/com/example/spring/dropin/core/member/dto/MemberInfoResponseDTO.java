package com.example.spring.dropin.core.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberInfoResponseDTO {
    private String userId;
    private String userName;
}

