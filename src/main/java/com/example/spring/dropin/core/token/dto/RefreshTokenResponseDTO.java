package com.example.spring.dropin.core.token.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RefreshTokenResponseDTO {
    private String accessToken;
}
