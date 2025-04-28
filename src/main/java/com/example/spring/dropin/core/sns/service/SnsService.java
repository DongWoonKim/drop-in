package com.example.spring.dropin.core.sns.service;

import com.example.spring.dropin.config.aws.s3.S3Uploader;
import com.example.spring.dropin.core.sns.dto.CreateSnsResponseDTO;
import com.example.spring.dropin.core.sns.repository.SnsRepository;
import com.example.spring.dropin.domain.Sns;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class SnsService {

    private final S3Uploader s3Uploader;
    private final SnsRepository snsRepository;

    public CreateSnsResponseDTO uploadSns(
        MultipartFile image,
        String content,
        String userId,
        String userName,
        Long boxId
    ) {
        try {
            // 🔥 S3 업로드
            String uploadPath = s3Uploader.upload(image, "posts");

            // 🔥 DB 저장
            Sns sns = Sns.builder()
                    .boxId(boxId)
                    .userId(userId)
                    .userName(userName)
                    .content(content)
                    .imageUrl(uploadPath) // 업로드된 S3 경로 저장
                    .build();

            snsRepository.save(sns);

            return CreateSnsResponseDTO.builder()
                    .message("업로드 완료")
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("파일 업로드 실패", e);
        }

    }

}
