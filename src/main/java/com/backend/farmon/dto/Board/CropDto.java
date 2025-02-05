package com.backend.farmon.dto.Board;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Getter
@Builder
public class CropDto {
    @NotEmpty // 리스트가 비어 있으면 안 됨
    private List<@NotBlank @Size(max = 100) String> names;
}
