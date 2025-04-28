package com.example.spring.dropin.core.sns.controller;

import com.example.spring.dropin.core.sns.dto.CreateSnsResponseDTO;
import com.example.spring.dropin.core.sns.dto.SnsResponseDTO;
import com.example.spring.dropin.core.sns.service.SnsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/communities")
public class SnsApiController {

    private final SnsService snsService;

    @GetMapping("/sns")
    public List<SnsResponseDTO> getSnsPosts(
            @RequestParam("box") Long box,
            @RequestParam(value = "lastCreatedAt", required = false) LocalDateTime lastCreatedAt,
            @RequestParam(value = "lastId", required = false) Long lastId, // 마지막 가져온 ID
            @RequestParam("size") int size // 한번에 가져올 개수
    ) {
        if (lastCreatedAt == null || lastId == null) {
            // 처음 요청: 최신순
            return snsService.findFirstPosts(box, size);
        } else {
            // 추가 요청: lastId보다 작은 것 가져오기
            return snsService.findNextPosts(box, lastCreatedAt, lastId, size);
        }
    }

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
