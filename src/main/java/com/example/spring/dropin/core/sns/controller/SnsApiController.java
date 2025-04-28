package com.example.spring.dropin.core.sns.controller;

import com.example.spring.dropin.core.sns.dto.CreateSnsResponseDTO;
import com.example.spring.dropin.core.sns.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class SnsApiController {

    private final SnsService snsService;

    @PostMapping
    public CreateSnsResponseDTO uploadSns() {



        return CreateSnsResponseDTO.builder()
                .message("업로드 완료")
                .build();
    }
}
