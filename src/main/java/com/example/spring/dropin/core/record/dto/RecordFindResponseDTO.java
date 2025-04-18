package com.example.spring.dropin.core.record.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RecordFindResponseDTO {

    private Long id;
    private String content;

    /** 빈 DTO를 재사용할 수 있도록 상수로 선언 */
    public static final RecordFindResponseDTO EMPTY = RecordFindResponseDTO.builder().build();

    /** 빈 DTO를 반환하는 팩토리 메서드 */
    public static RecordFindResponseDTO empty() {
        return EMPTY;
    }
}
