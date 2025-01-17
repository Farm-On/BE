package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ExchangeResponse {
    @Schema(description = "전환 성공 여부, 성공일 경우 true", example = "true")
    private Boolean isExchange;
}
