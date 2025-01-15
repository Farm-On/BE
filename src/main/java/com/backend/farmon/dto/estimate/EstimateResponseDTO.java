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
        private Long estimateId;
        private Long userId;
    }

    // 2) ReadListDto
    @Schema(description = "농사 견적서 리스트 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ListDTO {
        private Integer listSize;
        private Integer totalPage;
        private Long totalElements;
        private Integer currentPage;
        private Boolean isFirst;
        private Boolean isLast;

        private List<PreviewDTO> estimateList; // 실제로는 List<SomeEstimateData> 형태
    }
    // 3) DetailDTO
    @Schema(description = "농사 견적서 상세정보 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetailDTO {
        private Long estimateId;
        private Long userId;
        private String cropName;
        private String cropCategory;
        private String userName;
        private String category;
        private String address;
        private String budget;
        private String body;
        private LocalDate createDate;
    }

    // 4) UpdateDto
    @Schema(description = "농사 견적서 수정 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UpdateDTO {
        private Long estimateId;
        private Long userId;
    }

    // 5) DeleteDto
    @Schema(description = "농사 견적서 삭제 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DeleteDTO {
        private Long estimateId;
        private boolean deleted;
    }

    @Schema(description = "농사 견적서 목록용 미리보기 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PreviewDTO {
        private Long estimateId;
        private String cropName;
        private String cropCategory;
        private String category;
        private String address;
        private String budget;
        private LocalDate createdAt;
        private int isComplete;
    }



}
