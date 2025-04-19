package com.example.spring.dropin.util;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CookieUtilTest {

    @Test
    void getRefreshTokenFromCookies_메서드_테스트() {
        // given
        MockHttpServletRequest req = new MockHttpServletRequest();
        req.setCookies(
                new Cookie("refreshToken", "1234")
        );

        // when
        String refreshToken = CookieUtil.getRefreshTokenFromCookies(req);

        // then
        assertEquals("1234", refreshToken);

    }

}