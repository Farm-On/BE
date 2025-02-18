package com.backend.farmon.dto.post;

import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;

@Schema(name = "게시판 타입")
public enum PostType {
    ALL("전체"),
    POPULAR("인기"),
    QNA("Q&A"),
    FREE("자유게시판"),
    EXPERT_COLUMN("전문가 칼럼");

    private final String label;

    PostType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
