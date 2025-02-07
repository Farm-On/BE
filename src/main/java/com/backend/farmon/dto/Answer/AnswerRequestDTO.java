package com.backend.farmon.dto.Answer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "답변 요청 DTO")
public class AnswerRequestDTO {
    @Schema(description = "답변을 쓰는 사람의 Id",example = "3")
    Long userId;

    @Schema(description = "상위분야 ",example = "곡물")
    String category;

    @Schema(description = "하위분야",example = "쌀")
    String name;

    @Schema(description="QnA게시판 id",example = "1")
    Long boardId;

    @Schema(description="post 글 id",example = "1")
    Long postId;

    @Schema(description = "답변 제목",example = "농촌 에서 사과지배중인 청년 농부입니다.")
    private String title;

    @Schema(description = "답변 내용",example = "어떤 비료를 쓰는게 더 좋은 까요?" )
    private String content;


}
