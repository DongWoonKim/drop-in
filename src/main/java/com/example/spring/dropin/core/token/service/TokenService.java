package com.example.spring.dropin.core.token.service;

import com.example.spring.dropin.config.jwt.TokenProvider;
import com.example.spring.dropin.core.token.dto.RefreshTokenResponseDTO;
import com.example.spring.dropin.domain.Member;
import com.example.spring.dropin.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenProvider tokenProvider;

    public ResponseEntity<?> handleRefreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = CookieUtil.getRefreshTokenFromCookies(request);

        int valided = tokenProvider.validToken(refreshToken);

        log.info("Refresh token: {} valided: {}", refreshToken, valided);

        if (refreshToken != null && valided == 1) {
            Member member = tokenProvider.getTokenDetails(refreshToken);

            String newAccessToken = tokenProvider.generateToken(member, Duration.ofMinutes(1));
            String newRefreshToken = tokenProvider.generateToken(member, Duration.ofDays(3));

            CookieUtil.addCookie(response, "refreshToken", newRefreshToken, 7 * 24 * 60 * 60);
            response.setHeader("Authorization", "Bearer " + newAccessToken);

            return ResponseEntity.ok(RefreshTokenResponseDTO.builder().token(newAccessToken).build());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Refresh Token이 유효하지 않습니다.");
        }
    }


}
