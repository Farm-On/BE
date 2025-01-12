package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class ExchangeResponse {
    @Schema(description = "전환 성공 여부, 성공일 경우 true", example = "true")
    private Boolean isExchange;
}
