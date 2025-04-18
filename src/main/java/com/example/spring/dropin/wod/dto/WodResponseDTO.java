package com.example.spring.dropin.wod.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WodResponseDTO {
    private String title;
    private String program;

    /** 빈 DTO를 재사용할 수 있도록 상수로 선언 */
    public static final WodResponseDTO EMPTY = WodResponseDTO.builder().build();

    /** 빈 DTO를 반환하는 팩토리 메서드 */
    public static WodResponseDTO empty() {
        return EMPTY;
    }
}
