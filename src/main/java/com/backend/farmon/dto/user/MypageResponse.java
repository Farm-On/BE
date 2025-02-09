package com.backend.farmon.dto.user;

import com.backend.farmon.domain.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;

public class MypageResponse {

    @Builder
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "마이페이지 응답 DTO")
    public static class UserInfoDTO{
        String name;
        LocalDate birth;
        Gender gender;
        String phone;
        String email;
    }
}
