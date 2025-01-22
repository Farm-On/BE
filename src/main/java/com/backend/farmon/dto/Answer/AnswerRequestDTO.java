package com.backend.farmon.dto.Answer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "답변 요청 DTO")
public class AnswerRequestDTO {
    @Schema(description = "답변 제목")
    private String title;

    @Schema(description = "답변 내용")
    private String content;

    @Schema(description = "답변자 ID")
    private String answeredUserId; // 답변한 사람의 ID 추가
}
