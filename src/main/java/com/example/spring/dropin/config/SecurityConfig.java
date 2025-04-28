package com.example.spring.dropin.config;

import com.example.spring.dropin.config.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .requestMatchers(
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/favicon.ico"
                ); // 정적 리소스 경로 무시
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        session -> session.sessionCreationPolicy(STATELESS)
                ) // 세션 사용 안 함
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                    new AntPathRequestMatcher("/members/new", "GET"),
                                    new AntPathRequestMatcher("/members/login", "GET"),
                                    new AntPathRequestMatcher("/members/*/pending", "GET"),
                                    new AntPathRequestMatcher("/members/{userId}", "GET"),
                                    new AntPathRequestMatcher("/home", "GET"),
                                    new AntPathRequestMatcher("/individual", "GET"),
                                    new AntPathRequestMatcher("/group", "GET"),
                                    new AntPathRequestMatcher("/upload", "GET"),
                                    new AntPathRequestMatcher("/wods", "GET"),
                                    new AntPathRequestMatcher("/", "GET"),
                                    new AntPathRequestMatcher("/records", "GET"),
                                    new AntPathRequestMatcher("/records/me", "GET"),
                                    new AntPathRequestMatcher("/boxes", "GET"),
                                    new AntPathRequestMatcher("/actuator/health", "GET"),
                                    new AntPathRequestMatcher("/members", "POST"),
                                    new AntPathRequestMatcher("/members/login", "POST"),
                                    new AntPathRequestMatcher("/members/logout", "POST"),
                                    new AntPathRequestMatcher("/records/me", "POST"),
                                    new AntPathRequestMatcher("/refresh-token", "POST"),
                                    new AntPathRequestMatcher("/communities/posts", "POST")
                                )
                                .permitAll()
                                .anyRequest().authenticated())
                .logout(AbstractHttpConfigurer::disable)
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
