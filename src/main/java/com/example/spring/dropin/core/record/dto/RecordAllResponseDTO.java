package com.example.spring.dropin.core.record.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecordAllResponseDTO {
    private String userId;
    private String content;
}
