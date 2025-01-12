package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.user.MypageRequest;
import com.backend.farmon.dto.user.MypageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "사용자 정보 설정")
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

}
