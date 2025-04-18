package com.example.spring.dropin.core.wod.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString
@NoArgsConstructor
public class WodRequestDTO {
    private String date;
    private String box;

}
