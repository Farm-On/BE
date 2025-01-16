package com.backend.farmon.dto.FieldFilterDTO;

import java.util.List;

public class LocationFilterDTO {
    private String region; // 예: 서울, 경기 등
    private List<String> districts; // 지역 목록 (예: 서울전체, 강남구 등)
}
