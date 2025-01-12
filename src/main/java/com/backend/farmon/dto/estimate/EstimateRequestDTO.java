package com.backend.farmon.dto.estimate;

import io.swagger.v3.oas.annotations.media.Schema;
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
        private Long cropId;
        private Long userId;
        private String category;
        private String address;
        private String budget;
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
        private Long cropId;
        private Long userId;
        private String category;
        private String address;
        private String budget;
        private String body;
    }

}
