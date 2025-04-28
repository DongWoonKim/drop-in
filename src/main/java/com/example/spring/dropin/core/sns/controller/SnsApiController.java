package com.example.spring.dropin.core.sns.controller;

import com.example.spring.dropin.core.sns.dto.CreateSnsResponseDTO;
import com.example.spring.dropin.core.sns.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class SnsApiController {

    private final SnsService snsService;

    @PostMapping("/posts")
    public CreateSnsResponseDTO uploadSns(
            @RequestParam("image") MultipartFile image,
            @RequestParam("content") String content,
            @RequestParam("userId") String userId,
            @RequestParam("userName") String userName,
            @RequestParam("boxId") Long boxId
    ) {
        return snsService.uploadSns(image, content, userId, userName, boxId);
    }
}
