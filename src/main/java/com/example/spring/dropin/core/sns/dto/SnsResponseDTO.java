package com.example.spring.dropin.core.sns.dto;

import com.example.spring.dropin.domain.Sns;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class SnsResponseDTO {
    private Long id;
    private String userName;
    private String content;
    private String imageUrl;
    private LocalDateTime createdAt;

    public static SnsResponseDTO from(Sns sns) {
        return SnsResponseDTO.builder()
                .id(sns.getId())
                .userName(sns.getUserName())
                .content(sns.getContent())
                .imageUrl(sns.getImageUrl())
                .createdAt(sns.getCreatedAt())
                .build();
    }
}
