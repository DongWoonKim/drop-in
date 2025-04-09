package com.example.spring.dropin.domain;

import com.example.spring.dropin.dto.JoinResponseDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.*;

@Entity
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "user_id", length = 30, nullable = false, unique = true)
    private String userId;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(name = "user_name", length = 20, nullable = false)
    private String userName;

    @Column(length = 20, nullable = false)
    private String role;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(length = 15, nullable = false)
    private String phone;

    @Column(length = 100, nullable = false)
    private String box;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(nullable = false)
    private LocalDate birth;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @PrePersist
    public void prePersist() {
        if (this.role == null) {
            this.role = "ROLE_USER";
        }
        if (this.status == null) {
            this.status = "PENDING";
        }
    }

    public JoinResponseDTO toDTO() {
        return JoinResponseDTO.builder()
                .build();
    }

}
