package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원가입")
@RestController
@RequiredArgsConstructor
public class SignupController {

    @PostMapping("/api/user/join")
    @Operation(summary = "농업인 회원가입 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<SignupResponse.JoinResultDTO> userJoin(@RequestBody SignupRequest.UserJoinDto userJoinDto){

        return null;
    }

    @PostMapping("/api/expert/join")
    @Operation(summary = "전문가 회원가입 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<SignupResponse.JoinResultDTO> expertJoin(@RequestBody SignupRequest.ExpertJoinDto expertJoinDto){

        return null;
    }

    @PatchMapping("/api/withdraw/{id}")
    @Operation(summary = "회원탈퇴 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<SignupResponse.WithdrawDTO> withdraw(@PathVariable String id) {
        return null;
    }
}
