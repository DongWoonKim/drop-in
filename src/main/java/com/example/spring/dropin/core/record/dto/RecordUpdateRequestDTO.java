package com.example.spring.dropin.core.record.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RecordUpdateRequestDTO {
    private Long recordId;
    private String userId;
    private String content;
}
