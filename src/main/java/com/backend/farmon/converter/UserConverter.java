package com.backend.farmon.converter;

import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.ExpertCareer;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.enums.Gender;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.expert.ExpertCareerResponse;
import com.backend.farmon.dto.user.ExchangeResponse;
import com.backend.farmon.dto.user.MypageResponse;
import com.backend.farmon.dto.user.SignupRequest;

import java.time.LocalDate;

public class UserConverter {
    public static ExchangeResponse toExchangeResponse(Long userId, String role, Expert expert, String token) {
        ExchangeResponse.ExchangeResponseBuilder responseBuilder = ExchangeResponse.builder()
                .userId(userId)
                .exchangeRole(role)
                .token(token);

        // 전문가로 등록되어 있는 사용자라면 전문가 아이디도 반환
        if (expert != null) {
            responseBuilder.expertId(expert.getId());
        }

        return responseBuilder.build();
    }

    // 유저 마이페이지 응답 DTO 생성
    public static MypageResponse.UserInfoDTO toUserInfoGetResultDTO(User user) {
        return MypageResponse.UserInfoDTO.builder()
                .name(user.getUserName())
                .birth(user.getBirthDate())
                .gender(user.getGender())
                .phone(user.getPhoneNum())
                .email(user.getEmail())
                .build();
    }
}