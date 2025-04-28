package com.example.spring.dropin.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sns")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Sns {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "box_id", nullable = false)
    private Long boxId;

    @Column(name = "user_id", nullable = false, length = 30)
    private String userId;

    @Column(name = "user_name", nullable = false, length = 30)
    private String userName;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "image_url", length = 500)
    private String imageUrl;

    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = java.time.LocalDateTime.now();
    }
}
