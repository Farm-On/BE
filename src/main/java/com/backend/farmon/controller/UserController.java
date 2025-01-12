package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.user.ExchangeResponse;
import com.backend.farmon.dto.user.MypageRequest;
import com.backend.farmon.dto.user.MypageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 정보")
@RestController
@RequiredArgsConstructor
public class UserController {

    @GetMapping("/user/{id}")
    @Operation(summary = "마이페이지 조회 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<MypageResponse.UserInfoDTO> getUserInfo(@PathVariable Long id){

        return null;
    }

    @PatchMapping("/user/{id}")
    @Operation(summary = "마이페이지 수정 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "BAD REQUEST, 잘못된 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON404", description = "NOT FOUND, 리소스를 찾을 수 없음")
    })
    public ApiResponse<MypageResponse> updateUserInfo(
            @PathVariable Long id,
            @RequestBody MypageRequest.UpdateUserInfoDTO updateUserInfoDTO
    ) {
        return null;
    }

    @PatchMapping("/user/exchange")
    @Operation(
            summary = "사용자 역할 전환 API",
            description = "농업인 전환과 전문가 전환에 관한 API 입니다. 농업인일 경우 전문가로 전환, 전문가일 경우 농업인으로 전환됩니다. " +
                    "유저 아이디, 사용자 유형을 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "type", description = "사용자 유형(농업인 or 전문가)", example = "농업인")
    })
    public ApiResponse<ExchangeResponse> patchUserRole(@RequestParam(name="userId") Long userId,
                                                       @RequestParam(name="type") String type) {
        return null;
    }
}
