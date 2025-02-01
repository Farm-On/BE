package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ExpertDetailRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 추가정보 편집 요청 DTO")
    public static class ExpertDetailPostDTO {
        @Schema(description = "추가정보 내용", example = "농업기술센터 병해충 방제 특강 강사 활동")
        @NotBlank
        String content;
    }
}
