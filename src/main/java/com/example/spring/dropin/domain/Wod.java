package com.example.spring.dropin.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@Builder
@ToString
@DynamicInsert
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "wod")
public class Wod {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(length = 200, nullable = false)
    private String title;

    @Column(nullable = false)
    private String program;

    @Column(length = 100, nullable = false)
    private String box;

    /**
     * • columnDefinition으로 DB 기본값 선언
     * • @DynamicInsert 덕분에 이 필드가 null일 때는 INSERT SQL에 컬럼이 빠지고,
     *   값을 세팅하면 그대로 INSERT 될 수 있습니다.
     */
    @Setter
    @Column(nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime created;

}
