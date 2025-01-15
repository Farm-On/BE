package com.backend.farmon.dto.FieldFilterDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

public class KnowHowFilterDTO {
    private String KnowHowName;
    private List<SubCategoryDTO> subCategories;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubCategoryDTO {
        private String subCategoryName;
        private Boolean isSelected;
    }
}
