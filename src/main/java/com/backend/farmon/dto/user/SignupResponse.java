package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class SignupResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "회원가입 성공시 응답 DTO")
    public static class JoinResultDTO{
        Long userId;
        LocalDateTime createdAt;
    }
}
