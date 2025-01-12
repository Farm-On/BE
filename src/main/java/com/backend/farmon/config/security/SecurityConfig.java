package com.backend.farmon.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity // Spring Security 설정 활성화
@Configuration
public class SecurityConfig { // 애플리케이션의 보안 정책을 정의

    // SecurityFilterChain 정의
    // 추후에 농업인 관련 페이지는 농업인만, 전문가 관련 페이지는 전문가만 가입할 수 있도록 수정 필요
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .anyRequest().permitAll() // 모든 요청을 인증 없이 접근 가능하도록 설정
                )
                .csrf().disable(); // 필요 시 CSRF 보호 비활성화

        return http.build();
    }

    // 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
