package com.backend.farmon.dto.expert;

import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.PortfolioImg;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

public class PortfolioRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostPortfolioDTO { // 포트폴리오 등록 요청 DTO
        @Schema(description = "포트폴리오 제목", example = "무양 토양센서 컨설팅")
        String title;
        @Schema(description = "내용", example = "<h1>제목</h1>\n" +
                "<p>여기에 <strong>강조된 텍스트</strong>가 포함되어 있습니다.</p>\n" +
                "<img src=\"http://example.com/image.jpg\" alt=\"이미지\">\n")
        String text;
    }
}
