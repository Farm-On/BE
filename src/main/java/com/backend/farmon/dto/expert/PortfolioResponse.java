package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class PortfolioResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 포트폴리오 응답 DTO")
    public static class PostPortfolioResultDTO {
        @Schema(description = "생성된 포트폴리오 아이디")
        Long portfolioId;

        @Schema(description = "포트폴리오 제목")
        String title;

        @Schema(description = "내용")
        String text;

        @Schema(description = "대표 이미지")
        String thumbnailImg;

        @Schema(description = "생성시간")
        LocalDateTime createdAt;
    }

}
