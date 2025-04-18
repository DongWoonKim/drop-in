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

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        log.info("request: {}", request.getRequestURI());

        String token = resolveToken(request);
        log.info("{}, token: {}",tokenProvider.validToken(token), token);

        if (token != null && tokenProvider.validToken(token) == 1) {
            // 토큰이 유효할 경우, 인증 정보를 설정
            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Member member = tokenProvider.getTokenDetails(token);
            request.setAttribute("member", member);

        } else if (token != null && tokenProvider.validToken(token) == 2) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return; // 더 이상 진행하지 않음
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
}
