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
        String areaName;
        String areaNameDetail;
        String budget;
        String title;
        String body;
        String createdDate;

        // 이미지 URL 목록 추가
        List<String> imageUrls;
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
    @Schema(description = "견적찾기 농사 견적서 리스트 응답 DTO")
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

    // 5-1) ReadFilteredListDto
    @Schema(description = "견적찾기 농사 견적서 필터링된 리스트 응답 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class FilteredListDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Integer currentPage;
        Boolean isFirst;
        Boolean isLast;
        String estimateCategory;
        String budget;
        String areaName;
        String areaNameDetail;

        List<PreviewDTO> estimateList; // 실제로는 List<SomeEstimateData> 형태
    }

    // 6) ListDTO를 위한 Preview dto
    @Schema(description = "농사 견적서 목록용 미리보기 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class PreviewDTO {
        Long estimateId;
        String title;
        String cropName;
        String cropCategory;
        String estimateCategory;
        String areaName;
        String areaNameDetail;
        String budget;
        Integer status;
        String createdAt;
    }

    // 7) 견적서에 매핑된 채팅과 전문가 정보 dto
    @Schema(description = "견적서 상세 보기 밑 제안받은 견적용 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OfferDTO {
        Long estimateId;
        Long chatRoomId;
        Long expertId;
        String name;
        String nickname;
        Boolean isNicknameOnly;
        Float rating;
        String profileImageUrl;
        String description;
        Long consultingCount;
    }

    // 8) 견적서에 매핑된 채팅과 전문가 정보 dto
    @Schema(description = "제안받은 견젹 리스트 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class OfferListDTO {
        Integer listSize;

        List<OfferDTO> offerList;
    }

    // 9) 전문가 직접찾기용 전문가 카드 리스트 dto
    @Schema(description = "전문가 직접찾기용 전문가 카드 리스트 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExpertCardListDTO {
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Integer currentPage;
        Boolean isFirst;
        Boolean isLast;
        List<ExpertCardDTO> expertCardDTOList;
    }

    // 10) 전문가 직접찾기용 전문가 카드 데이터 dto
    @Schema(description = "전문가 직접찾기용 전문가 카드 DTO")
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ExpertCardDTO {
        Long expertId;
        String name;
        String nickname;
        Boolean isNicknameOnly;
        String cropCategory;
        String cropName;
        Float rating;
        Integer careerYears;
        String profileImageUrl;
    }

}
