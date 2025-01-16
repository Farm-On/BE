package com.backend.farmon.dto.FieldFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class AllFilterDTO {
    private String fieldName;
    private List<SubCategoryDTO> subCategories;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategoryDTO {
        private String subCategoryName; // 예: 쌀, 보리 등
        private Boolean isSelected; // 선택 여부
    }
}
