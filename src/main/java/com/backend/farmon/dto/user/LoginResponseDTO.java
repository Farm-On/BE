package com.backend.farmon.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Builder
@Getter
@Setter
public class LoginResponseDTO {
    private Long userId;
    private String token;
}
