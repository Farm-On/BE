package com.backend.farmon.converter;

import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.ExpertCareer;
import com.backend.farmon.domain.ExpertDatail;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.expert.*;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

public class ExpertConverter {

    // 전문가 경력 엔티티 생성
    public static ExpertCareer toExpertCareer(ExpertCareerRequest.ExpertCareerPostDTO request){
        return ExpertCareer.builder()
                .title(request.getTitle())
                .startYear(request.getStartYear())
                .startMonth(request.getStartMonth())
                .endYear(request.getEndYear())
                .endMonth(request.getEndMonth())
                .isOngoing(request.getIsOngoing())
                .detailContent1(request.getDetailContent1())
                .detailContent2(request.getDetailContent2())
                .detailContent3(request.getDetailContent3())
                .detailContent4(request.getDetailContent4())
                .build();
    }

    // 전문가 경력 POST 응답 DTO 생성
    public static ExpertCareerResponse.PostExpertCareerResultDTO toExpertCareerPostResultDTO(ExpertCareer expertCareer){
        return ExpertCareerResponse.PostExpertCareerResultDTO.builder()
                .expertCareerId(expertCareer.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 전문가 경력 GET 응답 DTO 생성
    public static ExpertCareerResponse.GetExpertCareerResultDTO toExpertCareerGetResultDTO(ExpertCareer request){
        return ExpertCareerResponse.GetExpertCareerResultDTO.builder()
                .expertId(request.getExpert().getId())
                .title(request.getTitle())
                .startYear(request.getStartYear())
                .startMonth(request.getStartMonth())
                .endYear(request.getEndYear())
                .endMonth(request.getEndMonth())
                .isOngoing(request.getIsOngoing())
                .detailContent1(request.getDetailContent1())
                .detailContent2(request.getDetailContent2())
                .detailContent3(request.getDetailContent3())
                .detailContent4(request.getDetailContent4())
                .build();
    }

    // 전문가 경력 엔티티 업데이트
    public static ExpertCareer updateExpertCareer(ExpertCareer expertCareer, ExpertCareerRequest.ExpertCareerPostDTO expertCareerPostDTO){
        // 수정하는거라 빌더가 아니라 기존 엔티티 정보만 수정
        if (expertCareerPostDTO.getTitle() != null) {expertCareer.setTitle(expertCareerPostDTO.getTitle());}
        if (expertCareerPostDTO.getStartYear() != null) {expertCareer.setStartYear(expertCareerPostDTO.getStartYear());}
        if (expertCareerPostDTO.getStartMonth() != null) {expertCareer.setStartMonth(expertCareerPostDTO.getStartMonth());}
        if (expertCareerPostDTO.getEndYear() != null) {expertCareer.setEndYear(expertCareerPostDTO.getEndYear());}
        if (expertCareerPostDTO.getEndMonth() != null) {expertCareer.setEndMonth(expertCareerPostDTO.getEndMonth());}
        if (expertCareerPostDTO.getIsOngoing() != null) {expertCareer.setIsOngoing(expertCareerPostDTO.getIsOngoing());}
        if (expertCareerPostDTO.getDetailContent1() != null) {expertCareer.setDetailContent1(expertCareerPostDTO.getDetailContent1());}
        if (expertCareerPostDTO.getDetailContent2() != null) {expertCareer.setDetailContent2(expertCareerPostDTO.getDetailContent2());}
        if (expertCareerPostDTO.getDetailContent3() != null) {expertCareer.setDetailContent3(expertCareerPostDTO.getDetailContent3());}
        if (expertCareerPostDTO.getDetailContent4() != null) {expertCareer.setDetailContent4(expertCareerPostDTO.getDetailContent4());}

        return expertCareer;
    }

    // 전문가 추가정보 엔티티 생성
    public static ExpertDatail toExpertDetail(ExpertDetailRequest.ExpertDetailPostDTO request){
        return ExpertDatail.builder()
                .detailContent(request.getContent())
                .build();
    }

    // 전문가 추가정보 POST 응답 DTO 생성
    public static ExpertDetailResponse.PostExpertDetailResultDTO toExpertDetailPostResultDTO(ExpertDatail request){
        return ExpertDetailResponse.PostExpertDetailResultDTO.builder()
                .expertDetailId(request.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 전문가 추가정보 GET 응답 DTO 생성
    public static ExpertDetailResponse.GetExpertDetailResultDTO toExpertDetailGetResultDTO(ExpertDatail request){
        return ExpertDetailResponse.GetExpertDetailResultDTO.builder()
                .expertId(request.getExpert().getId())
                .content(request.getDetailContent())
                .build();
    }

    // 전문가 추가정보 엔티티 업데이트
    public static ExpertDatail updateExpertDetail(ExpertDatail expertDetail, ExpertDetailRequest.ExpertDetailPostDTO expertDetailPostDTO){
        if (expertDetailPostDTO.getContent() != null) {expertDetail.setDetailContent(expertDetailPostDTO.getContent());}

        return expertDetail;
    }

    // 전문가 대표서비스 수정 응답 DTO 생성
    public static ExpertProfileResponse.ResultUpdateSpecialtyDTO updateSpecialtyDTO(Expert request){
        return ExpertProfileResponse.ResultUpdateSpecialtyDTO.builder()
                .expertId(request.getId())
                .cropCategory(request.getCrop().getCategory())
                .crop(request.getCrop().getName())
                .serviceDetail1(request.getServiceDetail1())
                .serviceDetail2(request.getServiceDetail2())
                .serviceDetail3(request.getServiceDetail3())
                .serviceDetail4(request.getServiceDetail4())
                .build();
    }

    // 전문가 활동지역 수정 응답 DTO 생성
    public static ExpertProfileResponse.ResultUpdateAreaDTO updateAreaDTO(Expert request) {
        return ExpertProfileResponse.ResultUpdateAreaDTO.builder()
                .expertId(request.getId())
                .areaName(request.getArea().getAreaName())
                .areaNameDetail(request.getArea().getAreaNameDetail())
                .availableRange(request.getAvailableRange())
                .isAvailableEverywhere(request.getIsAvailableEverywhere())
                .isExcludeIsland(request.getIsExcludeIsland())
                .build();
    }
}
