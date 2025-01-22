package com.backend.farmon.dto.user;

import com.backend.farmon.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class SignupRequest {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "농업인(일반 회원) 회원가입 요청 DTO")
    public static class UserJoinDto{
        @Schema(description = "이름", example = "홍길동")
        @NotBlank
        String name;

        @Schema(description = "생년월일", example = "2025-01-01")
        @NotNull
        LocalDate birth;

        @Schema(description = "성별 (MALE,FEMALE) ", example = "MALE")
        @NotNull
        Gender gender;

        @Schema(description = "이메일 주소", example = "umc@gmail.com")
        @NotBlank
        @Email
        String email;

        @Schema(description = "비밀번호", example = "qwer1234")
        @NotBlank
        String password;

        @Schema(description = "휴대전화번호", example = "01012345678")
        @NotBlank
        String phone;
    }


    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "전문가 회원가입 요청 DTO")
    public static class ExpertJoinDto{
        @Schema(description = "이름", example = "홍길동")
        String name;

        @Schema(description = "생년월일", example = "2025-01-01")
        LocalDate birth;

        @Schema(description = "성별 (MALE,FEMALE) ", example = "MALE")
        String gender; // String으로 받되, 후에 Enum으로 변환

        @Schema(description = "이메일 주소", example = "umc@gmail.com")
        String email;

        @Schema(description = "비밀번호", example = "qwer1234")
        String password;

        @Schema(description = "휴대전화번호", example = "01012345678")
        String phone;

        @Schema(description = "전문가 전문 분야 카테고리 리스트")
        List<Long> expertCrops; // 전문가 전문 분야 카테고리 리스트

        @Schema(description = "전문가 활동 위치 카테고리 리스트")
        List<Long> expertLocations; // 전문가 활동 위치 카테고리 리스트
    }
}
