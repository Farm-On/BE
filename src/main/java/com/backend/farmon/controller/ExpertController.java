package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.expert.ExpertListResponse;
import com.backend.farmon.dto.expert.ExpertProfileResponse;
import com.backend.farmon.dto.user.MypageRequest;
import com.backend.farmon.dto.user.MypageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "전문가")
@RestController
@RequiredArgsConstructor
public class ExpertController {

    // 전문가 프로필 목록 조회
    @GetMapping("/expert")
    @Operation(
            summary = "전문가 프로필 목록 조회 API",
            description = "전문가 프로필 목록을 조회하는 API이며, 페이징을 포함합니다. " +
                    "서비스, 지역, 페이지를 query String 으로 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "service", description = "전문가 서비스 필터링"),
            @Parameter(name = "area", description = "전문가 지역 필터링"),
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작입니다.", example = "1", required = true)
    })
    public ApiResponse<ExpertListResponse.ExpertProfileListDTO> getExpertList (@RequestParam(name = "service") Long service,
                                                                               @RequestParam(name = "area") String area,
                                                                               @RequestParam(name = "page")  Integer page){
        return null;
    }

    // 전문가 내 프로필 페이지 조회
    @PatchMapping("/expert/{id}")
    @Operation(summary = "전문가 내 프로필 페이지 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
    })
    public ApiResponse<ExpertProfileResponse.ExpertProfileDTO> getExpertProfilePage(@PathVariable Long id) {
        return null;
    }
}
