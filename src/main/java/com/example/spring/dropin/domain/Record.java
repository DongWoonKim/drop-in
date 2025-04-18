package com.example.spring.dropin.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 30, nullable = false)
    private String userId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /** 박스 이름 (VARCHAR(100)) */
    @Column(length = 100, nullable = false)
    private String box;

    /** 기록 날짜 */
    @Column(name = "date", nullable = false)
    private LocalDate date;
}
