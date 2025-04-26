package com.example.spring.dropin.core.record.dto;

import com.example.spring.dropin.domain.Record;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class RecordSaveRequestDTO {
    private String userId;
    private String date;
    @Setter
    private String content;
    private String box;

    public Record toRecord() {
        return Record.builder()
                .userId(userId)
                .content(content)
                .date(LocalDate.parse(date))
                .box(box)
                .build();
    }
}
