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
        @NotNull(message = "작물 id는 필수입니다.")
        private Long cropId;

        @NotNull(message = "유저 id는 필수입니다.")
        private Long userId;

        @NotNull(message = "견적 카테고리는 필수입니다.")
        private String category;

        @NotNull(message = "지역 id는 필수입니다.")
        private Long areaId;

        @NotNull(message = "상세 주소는 필수입니다.")
        private String addressDetail;

        @NotNull(message = "예산은 필수입니다.")
        private String budget;

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
        @NotNull(message = "작물 id는 필수입니다.")
        private Long cropId;

        @NotNull(message = "유저 id는 필수입니다.")
        private Long userId;

        @NotNull(message = "견적 카테고리는 필수입니다.")
        private String category;

        @NotNull(message = "상세 주소는 필수입니다.")
        private String address;

        @NotNull(message = "예산은 필수입니다.")
        private String budget;

        @NotNull(message = "견적 상세설명은 필수입니다.")
        private String body;
    }

}
