package com.example.spring.dropin.config.aws.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class S3Uploader {

    private final S3Client s3Client;
    private final String bucketName = "daily-pr";

    public String upload(MultipartFile file, String dirName) throws IOException {
        // 파일 이름 랜덤하게
        String originalFilename = file.getOriginalFilename();
        String extension = getFileExtension(originalFilename);
        String key = dirName + "/" + UUID.randomUUID() + "." + extension;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()));

            return key; // S3에 저장된 경로를 리턴
        } catch (S3Exception e) {
            throw new RuntimeException("S3 파일 업로드 실패: " + e.awsErrorDetails().errorMessage(), e);
        }
    }

    private String getFileExtension(String fileName) {
        int idx = fileName.lastIndexOf('.');
        if (idx == -1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다: " + fileName);
        }
        return fileName.substring(idx + 1);
    }
}
