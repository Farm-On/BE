package com.backend.farmon.dto.estimate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Schema(description = "농사 견적서 관련 요청 DTO")
public class EstimateRequestDTO {

    // 1) CreateDto
    @Schema(description = "농사 견적서 작성 요청 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateDTO {
        @Schema(description = "현재 유저 ID", example = "1")
        @NotNull(message = "유저 id는 필수입니다.")
        private Long userId;

        @Schema(description = "작물 ID", example = "쌀")
        @NotNull(message = "작물 이름은 필수입니다.")
        private String cropName;

        @Schema(description = "견적 카테고리", example = "작물 관리")
        @NotNull(message = "견적 카테고리는 필수입니다.")
        private String category;

        @Schema(description = "지역 이름", example = "서울")
        @NotNull(message = "지역 이름은 필수입니다.")
        private String areaName;

        @Schema(description = "지역 세부 이름", example = "강남구")
        @NotNull(message = "지역 세부 이름은 필수입니다.")
        private String areaNameDetail;

        @Schema(description = "예산 범위", example = "50만원 ~ 100만원")
        @NotNull(message = "예산은 필수입니다.")
        private String budget;

        @Schema(description = "견적요청서 제목", example = "제목입력")
        @NotNull(message = "제목은 필수입니다.")
        private String title;

        @Schema(description = "견적요청서 상세 설명", example = "상세 설명 입력")
        @NotNull(message = "견적 상세설명은 필수입니다.")
        private String body;
    }

    // 2) UpdateDto
    @Schema(description = "농사 견적서 수정 요청 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateDTO {
        @NotNull(message = "유저 id는 필수입니다.")
        private Long userId;

        @NotNull(message = "작물 이름은 필수입니다.")
        private String cropName;

        @NotNull(message = "견적 카테고리는 필수입니다.")
        private String category;

        @NotNull(message = "지역 이름은 필수입니다.")
        private String areaName;

        @NotNull(message = "지역 세부 이름은 필수입니다.")
        private String areaNameDetail;

        @NotNull(message = "예산은 필수입니다.")
        private String budget;

        @NotNull(message = "제목은 필수입니다.")
        private String title;

        @NotNull(message = "견적 상세설명은 필수입니다.")
        private String body;
    }

    @Schema(description = "농사 견적서 필터링 요청 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FilterDTO {
        private String estimateCategory;
        private String budget;
        private String areaName;
        private String areaNameDetail;

    }
}
