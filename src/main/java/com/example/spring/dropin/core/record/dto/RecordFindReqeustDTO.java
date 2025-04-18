package com.example.spring.dropin.core.record.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
public class RecordFindReqeustDTO {
    private String userId;
    private String date;
    private String box;
}
