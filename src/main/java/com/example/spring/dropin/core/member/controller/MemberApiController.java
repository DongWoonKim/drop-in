package com.example.spring.dropin.core.member.controller;

import com.example.spring.dropin.config.jwt.TokenProvider;
import com.example.spring.dropin.core.member.dto.*;
import com.example.spring.dropin.core.member.service.MemberService;
import com.example.spring.dropin.domain.Member;
import com.example.spring.dropin.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberApiController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping
    public JoinResponseDTO join(@RequestBody JoinRequestDTO joinRequestDTO) {
        return Optional.ofNullable(memberService.registerMember(joinRequestDTO.toEntity(bCryptPasswordEncoder)))
                .map(savedMember -> JoinResponseDTO.builder().success(false).build())
                .orElse(JoinResponseDTO.builder().success(true).build());
    }

    @PostMapping("/login")
    public LoginResponseDTO login(
            HttpServletResponse response,
            @RequestBody LoginRequestDTO loginRequestDTO
    ) {
        Member member = memberService.loginMember(loginRequestDTO.toMember());

        // Access Token 생성 (짧은 유효기간)
        String accessToken = tokenProvider.generateToken(member, Duration.ofDays(1));

        // Refresh Token 생성 (긴 유효기간)
        String refreshToken = tokenProvider.generateToken(member, Duration.ofDays(10));

        // Refresh Token을 HttpOnly 쿠키에 저장
        CookieUtil.addCookie(response, "refreshToken", refreshToken, 7 * 24 * 60 * 60);

        return LoginResponseDTO.builder()
                .accessToken(accessToken)
                .build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, "refreshToken");
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public MemberInfoResponseDTO getMyInfo(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replaceFirst("^Bearer\\s+", "");
        Member tokenDetails = tokenProvider.getTokenDetails(token);

        return MemberInfoResponseDTO.builder()
                .userId(tokenDetails.getUserId())
                .userName(tokenDetails.getUserName())
                .build();
    }

    @GetMapping("/{userId}/status")
    public UserStatusResponseDTO getMemberStatus(@PathVariable("userId") String userId) {
        return UserStatusResponseDTO.builder()
                .status( memberService.getUserStatus(userId).getStatus() )
                .build();
    }
}
