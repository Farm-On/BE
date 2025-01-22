package com.backend.farmon.dto.Board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BoardRequestDto {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "모든 게시글에 공통된 속성을 가진 기본 게시글 클래스입니다.")
    public static abstract class BasePost {
        @Schema(description = "게시글 제목", example = "게시글 제목 예시")
        private String postTitle;

        @Schema(description = "게시글 내용", example = "게시글 내용 예시")
        private String postContent;

        @Schema(description = "작성자 ID (userNo)", example = "12345")
        private Long userNo;

        @Schema(description = "게시판 ID (boardNo)", example = "67890")
        private Long boardNo;

        @Schema(description = "게시글에 대한 좋아요 수", example = "10")
        private int likeCount;

        @Schema(description = "게시글에 대한 댓글 수", example = "5")
        private int commentCount;

        @Schema(description = "게시글 종류", example = "QNA")
        private PostType postType;
    }

    @Schema(description = "게시글 종류")
    public enum PostType {
        ALL, POPULAR, QNA, EXPERT_LOUNGE, EXPERT_COLUMN
    }

    @Data
    @Schema(description = "전체게시판에 있는 글")
    public static class AllPost extends BasePost {
    }

    @Data
    @Schema(description = "인기 게시글")
    public static class PopularPost extends BasePost {
    }

    @Data
    @Schema(description = "QnA 게시판에 관련된 게시글")
    public static class QnaPost extends BasePost {
    }

    @Data
    @Schema(description = "전문가 라운지 게시글")
    public static class ExpertLounge extends BasePost {
        @Schema(description = "지역 ID", example = "123")
        private Long areaId;
    }

    @Data
    @Schema(description = "전문가 칼럼 게시글")
    public static class ExpertColumn extends BasePost {
    }
}
