package com.backend.farmon.dto.Board;

import com.backend.farmon.dto.post.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class BoardRequestDto {

    // BasePost는 모든 게시글의 공통적인 속성을 담는 추상 클래스입니다.
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "모든 게시글에 공통된 속성을 가진 기본 게시글 클래스입니다.")
    public static abstract class BasePost {
        @Schema(description = "게시글 제목", example = "게시글 제목 예시")
        private String postTitle;   // 게시글 제목

        @Schema(description = "게시글 내용", example = "게시글 내용 예시")
        private String postContent; // 게시글 내용

        @Schema(description = "작성자 ID (userNo)", example = "12345")
        private Long userNo;        // 작성자 ID (userNo)

        @Schema(description = "게시판 ID (boardNo)", example = "67890")
        private Long boardNo;       // 게시판 ID (boardNo)

        @Schema(description = "게시글에 대한 좋아요 수", example = "10")
        private int like;           // 좋아요 수

        @Schema(description = "게시글에 대한 댓글 수", example = "5")
        private int comment;        // 댓글 수

        @Schema(description = "게시글 종류 (예: QnA, 일반 게시글 등)", example = "QnA")
        public PostType postType;   // 게시글 종류 (예: QnA, 일반 게시글 등)
    }

    // AllPost는 모든 종류의 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @Schema(description = "전체 게시판에 있는 글")
    public static class AllPost extends BasePost {
        // AllPost에는 추가적인 필드나 메서드가 필요할 경우 이곳에 작성
    }

    // PopularPost는 인기 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @Schema(description = "인기 게시판에 있는 글")
    public static class PopularPost extends BasePost {
        // PopularPost에는 추가적인 필드나 메서드가 필요할 경우 이곳에 작성
    }

    //QnaPost는 질문게시판
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QnaPost extends BasePost {
        // 필요한 경우 추가 필드 선언
    }

    //FreePost 자유게시판
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FreePost extends BasePost {
        // 필요한 경우 추가 필드 선언
    }

    // ExpertColumn은 전문가 칼럼 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @NoArgsConstructor
    @Schema(description = "전문가 칼럼 게시글")
    public static class ExpertColumn extends BasePost {
        @Schema(description = "분야 ID", example = "123")
        private Long FilterId; // 지역 ID
    }
}
