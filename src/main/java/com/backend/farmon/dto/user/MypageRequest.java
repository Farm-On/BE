package com.backend.farmon.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

public class MypageRequest {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL) // null 값인 필드는 JSON에 포함되지 않음
    @Schema(description = "마이페이지 수정 요청 DTO")
    public static class UpdateUserInfoDTO{
        @Schema(description = "이름", example = "홍길동")
        String name;

        @Schema(description = "생년월일", example = "2025-01-01")
        LocalDate birth;

        @Schema(description = "변경할 이메일 주소", example = "umc@gmail.com")
        String email;

        @Schema(description = "변경할 비밀번호", example = "qwer1234")
        String password;
    }


}
