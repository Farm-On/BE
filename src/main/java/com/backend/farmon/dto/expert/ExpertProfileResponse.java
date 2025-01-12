package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

public class ExpertProfileResponse {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpertProfileDTO {
        @Schema(description = "프로필 이미지")
        String profileImg;

        @Schema(description = "이름")
        String name;

        @Schema(description = "본인인증 여부")
        Boolean isVerified;

        @Schema(description = "전문가 한 줄 소개")
        String expertDescription;

        @Schema(description = "평점")
        Float rate;

        @Schema(description = "진행했던 컨설팅 수")
        Integer consultingCount;

        @Schema(description = "경력")
        Integer career;

        @Schema(description = "추가정보")
        String extraDetails;

        @Schema(description = "포트폴리오 목록 리스트")
        List<PortfolioDetailDTO> portfolio;

        @Schema(description = "전문가 전문 분야 카테고리 리스트")
        List<Long> expertCrops;

        @Schema(description = "전문가 활동 위치 카테고리 리스트")
        List<Long> expertLocations;

        @Schema(description = "활동 가능 범위")
        Integer availableRange;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PortfolioDetailDTO {
        @Schema(description = "포트폴리오 썸네일 이미지")
        String thumbnailImg;

        @Schema(description = "포트폴리오 제목")
        String name;

        LocalDate createdAt; // 기본 정렬 생성 날짜순
    }
}
