package com.backend.farmon.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "게시판 타입")
public enum PostType {
    ALL, POPULAR, QnA, EXPERT_LOUNGE, EXPERT_COLUMN;

    // QnA 게시판만 답변 유무를 체크하도록 하는 메서드
    public boolean isAnswerRequired() {
        return this == QnA;  // QnA일 경우에만 true 반환
    }
}
