package com.backend.farmon.dto.FieldFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FieldCategoryDTO {
    private String fieldName; // 예: 곡물, 채소작물 등
    private List<SubCategoryDTO> subCategories; // 하위 카테고리 목록

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategoryDTO {
        private String subCategoryName; // 예: 쌀, 보리 등
        private Boolean isSelected; // 선택 여부
    }
}
