package com.example.spring.dropin.config.jwt;

import com.example.spring.dropin.domain.Member;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private final JwtProperties jwtProperties;

    public String generateToken(Member member, Duration expiredAt) {
        Date now = new Date();
        return makeToken(
                member,
                new Date(now.getTime() + expiredAt.toMillis())
        );
    }

    public int validToken(String token) {
        try {
            getClaims(token);
            return 1;
        } catch (ExpiredJwtException e) {
            // 토큰이 만료된 경우
            return 2;
        } catch (Exception e) {
            // 복호화 과정에서 에러가 나면 유효하지 않은 토큰
            return 3;
        }
    }

    private String makeToken(Member member, Date expire) {

        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expire)
                .setSubject( member.getUserId() )
                .claim("id", member.getId())
                .claim("role", member.getRole())
                .claim("userName", member.getUserName())
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        // Claims에서 역할을 추출하고, GrantedAuthority로 변환
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority( claims.get("role").toString() ) // 역할을 GrantedAuthority로 변환
        );

        // UserDetails 객체 생성
        UserDetails userDetails = new User(claims.getSubject(), "", authorities);

        // UsernamePasswordAuthenticationToken 생성
        return new UsernamePasswordAuthenticationToken(userDetails, token, authorities);
    }

    // 토큰에서 Subject, id, role 값을 추출하는 메서드
    public Member getTokenDetails(String token) {
        Claims claims = getClaims(token);

        return Member.builder()
                .id(claims.get("id", Long.class))
                .userId(claims.getSubject())
                .userName(claims.get("userName", String.class))
                .role(claims.get("role", String.class))
                .build();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())  // SecretKey 객체 사용
                .build()                    // 파서 빌드
                .parseClaimsJws(token)      // 토큰 파싱
                .getBody();                 // 클레임 추출
    }

    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
