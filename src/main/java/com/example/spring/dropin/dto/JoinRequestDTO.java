package com.example.spring.dropin.dto;

import com.example.spring.dropin.domain.Member;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;

@Getter
@ToString
public class JoinRequestDTO {

    private String userId;
    private String password;
    private String userName;
    private LocalDate birthDate; // JSON에서 "yyyy-MM-dd" 형식으로 받을 수 있음
    private String phone;
    private String email;
    private String box;

    public Member toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
        return Member.builder()
                .userId(userId)
                .password(bCryptPasswordEncoder.encode(password))
                .userName(userName)
                .birth(birthDate)
                .phone(phone)
                .email(email)
                .box(box)
                .build();
    }


}
