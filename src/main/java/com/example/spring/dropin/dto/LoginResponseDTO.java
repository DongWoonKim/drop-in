package com.example.spring.dropin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDTO {
    private String accessToken;
}
