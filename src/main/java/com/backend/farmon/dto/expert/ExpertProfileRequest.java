package com.backend.farmon.dto.expert;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ExpertProfileRequest {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateSpecialtyDTO { // 대표 서비스 변경 요청 DTO
        @Schema(description = "작물 이름")
        String crop;

        @Schema(description = "대표 서비스 디테일1")
        String serviceDetail1;
        @Schema(description = "대표 서비스 디테일2")
        String serviceDetail2;
        @Schema(description = "대표 서비스 디테일3")
        String serviceDetail3;
        @Schema(description = "대표 서비스 디테일4")
        String serviceDetail4;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAreaDTO { // 활동지역 변경 요청 DTO
        @Schema(description = "위치 세부정보")
        String areaNameDetail;

        @Schema(description = "활동 가능 범위")
        String availableRange;
        @Schema(description = "전국 어디든 가능 여부")
        Boolean isAvailableEverywhere;
        @Schema(description = "도서 지방 제외 여부")
        Boolean isExcludeIsland;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateProfileDTO { // 내 프로필 변경 요청 DTO
        @Schema(description = "닉네임", example = "해충해방")
        String nickName;
        @Schema(description = "닉네임만 보여주기 여부", example = "true")
        Boolean isNickNameOnly;
        @Schema(description = "한 줄 소개", example = "현장에서 쌓은 경험을 바탕으로, 실전 노하우를 전해드립니다.")
        String expertDescription;
    }
}