package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.converter.SignupConverter;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import com.backend.farmon.service.ExpertService.ExpertCommandService;
import com.backend.farmon.service.UserService.UserCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "회원가입", description = "회원가입에 관한 API")
@RestController
@RequiredArgsConstructor
public class SignupController {
    private final UserCommandService userCommandService;
    private final ExpertCommandService expertCommandService;

    @PostMapping("/api/user/join")
    @Operation(summary = "농업인 회원가입 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<SignupResponse.JoinResultDTO> userJoin(@Valid @RequestBody SignupRequest.UserJoinDto userJoinDto){
        User user = userCommandService.joinUser(userJoinDto);
        return ApiResponse.onSuccess(SignupConverter.toJoinResultDTO(user));// 저장된 엔티티로 응답 DTO생성후 반환
    }

    @PostMapping("/api/expert/join")
    @Operation(summary = "전문가 회원가입 API")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    public ApiResponse<SignupResponse.ExpertJoinResultDTO> expertJoin(@RequestBody SignupRequest.ExpertJoinDto expertJoinDto){
        SignupResponse.ExpertJoinResultDTO resultDTO = expertCommandService.joinExpert(expertJoinDto);
        return ApiResponse.onSuccess(resultDTO);
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
