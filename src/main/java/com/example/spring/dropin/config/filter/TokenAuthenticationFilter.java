package com.example.spring.dropin.config.filter;

import com.example.spring.dropin.config.jwt.TokenProvider;
import com.example.spring.dropin.domain.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";
    // 토큰 인증에서 제외할 URI 목록
    private static final List<String> EXCLUDED_URIS = List.of(
            "/members/new",
            "/members/login",
            "/members/logout",
            "/refresh-token",
            "/boxes",
            "/actuator/health"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        log.info("request: {}", requestURI);
        // 예외 경로인 경우 토큰 검사 생략
        if (isExcluded(requestURI) && request.getMethod().matches("GET|POST")) {
            chain.doFilter(request, response);
            return;
        }

        String token = resolveToken(request);
        int tokenStatus = tokenProvider.validToken(token);
        log.info("{}, token: {}",tokenStatus, token);

        if (token != null && tokenStatus == 1) {
            // 토큰이 유효할 경우, 인증 정보를 설정
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = tokenProvider.getTokenDetails(token);
            request.setAttribute("member", member);

        } else if (token != null) {
            if (tokenStatus == 2) {
                // 만료된 토큰: 클라이언트가 refresh-token 요청하도록
                log.warn("Expired token, refresh needed.");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            } else if (tokenStatus == 3) {
                // 잘못된 토큰: 아예 접근 거부
                log.warn("Invalid token, possible tampering.");
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
            }

            return;
        }

        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // Authorization 헤더에서 JWT 토큰 추출
        String bearerToken = request.getHeader(HEADER_AUTHORIZATION);

        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isExcluded(String requestURI) {
        return EXCLUDED_URIS.stream().anyMatch(requestURI::equals);
    }
}
