package com.backend.farmon.dto.estimate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Schema(description = "농사 견적서 응답 DTO")
public class EstimateResponseDTO {

    // 1) CreateDto
    @Schema(description = "농사 견적서 작성 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreateDTO {
        Long estimateId;
        Long userId;
    }

    // 2) Read DetailDTO
    @Schema(description = "농사 견적서 상세정보 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetailDTO {
        Long estimateId;
        Long userId;
        String cropName;
        String cropCategory;
        String userName;
        String category;
        String address;
        String budget;
        String body;
        LocalDate createDate;
    }

    // 3) UpdateDto
    @Schema(description = "농사 견적서 수정 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateDTO {
        Long estimateId;
        Long userId;
    }

    // 4) DeleteDto
    @Schema(description = "농사 견적서 삭제 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DeleteDTO {
        Long estimateId;
        Boolean deleted;
    }
    // 5) ReadListDto
    @Schema(description = "전문가-견적찾기 농사 견적서 리스트 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ListDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Integer currentPage;
        Boolean isFirst;
        Boolean isLast;

        List<PreviewDTO> estimateList; // 실제로는 List<SomeEstimateData> 형태
    }

    // 6) ListDTO를 위한 Preview dto
    @Schema(description = "전문가-견적찾기 농사 견적서 목록용 미리보기 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PreviewDTO {
        Long estimateId;
        String cropName;
        String cropCategory;
        String estimateCategory;
        String areaName;
        String areaNameDetail;
        String budget;
        LocalDate createdAt;
        Integer isComplete;
    }



}
