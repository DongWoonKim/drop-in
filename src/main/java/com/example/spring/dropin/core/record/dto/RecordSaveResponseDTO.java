package com.example.spring.dropin.core.record.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RecordSaveResponseDTO {
    private String content;
}
