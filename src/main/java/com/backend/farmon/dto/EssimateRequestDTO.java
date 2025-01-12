package com.backend.farmon.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "농사 견적서 관련 요청 DTO")
public class EssimateRequestDTO {

    @Schema(description = "농사 견적서 작성/수정 요청 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EstimateCreateRequestDTO {
        private Long cropId;
        private Long userId;
        private String category;
        private String address;
        private String budget;
        private String body;
    }

}
