package com.example.spring.dropin.config.jwt;

import com.example.spring.dropin.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TokenProviderTest {

    private final String EXPIRED_TOKEN = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJpc3MiOiJ0ZXN0QGdtYWlsLmNvbSIsImlhdCI6MTc0NDk1OTQzOSwiZXhwIjoxNzQ0OTY2NjM5LCJzdWIiOiJ0ZXN0IiwiaWQiOjUsInJvbGUiOiJST0xFX1VTRVIiLCJ1c2VyTmFtZSI6InRlc3QifQ.DEbF8lmJRBtgX_JfsIE_22sc0XfA_lkYfUpuCK5lw_ba8ecA4sqpQ2M73JjEAI7tKrt2ZwTOz8FG_9mxklTTnA";
    @Autowired
    private TokenProvider tokenProvider;
    private Member member;

    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .role("ROLE_USER")
                .userId("testId")
                .userName("testName")
                .box("testBox")
                .build();
    }

    @Test
    void 만료된_토큰_검증_테스트() throws Exception {
        // when
        int resultNum = tokenProvider.validToken(EXPIRED_TOKEN);
        // then
        assertEquals(2, resultNum);
    }

    @Test
    void 토큰_발급_테스트() throws Exception {
        // when
        String token = tokenProvider.generateToken(member, Duration.ofMinutes(1));
        int resultNum = tokenProvider.validToken(token);
        // then
        assertEquals(1, resultNum);
    }

    @Test
    void 리프래시_토큰_발급_로직_테스트() throws Exception {
        String refreshToken = tokenProvider.generateToken(member, Duration.ofMinutes(2));
        int resultNum = tokenProvider.validToken(refreshToken);
        Member tokenDetails = tokenProvider.getTokenDetails(refreshToken);
        String newRefreshToken = tokenProvider.generateToken(tokenDetails, Duration.ofMinutes(5));
        int resultNum2 = tokenProvider.validToken(newRefreshToken);

        assertNotNull(refreshToken);
        assertEquals(1, resultNum);
        assertEquals("testId", tokenDetails.getUserId());
        assertEquals("testName", tokenDetails.getUserName());
        assertEquals("ROLE_USER", tokenDetails.getRole());
        assertNotNull(newRefreshToken);
        assertEquals(1, resultNum2);
        assertNotEquals(refreshToken, newRefreshToken);
    }

}