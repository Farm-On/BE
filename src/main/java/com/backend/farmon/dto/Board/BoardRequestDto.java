package com.backend.farmon.dto.Board;

import com.backend.farmon.dto.PostType;
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
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "전체게시판에 있는 글")
    public static class AllPost extends BasePost {
        // AllPost에는 추가적인 필드나 메서드가 필요할 경우 이곳에 작성
    }

    // PopularPost는 인기 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "인기 게시글")
    public static class PopularPost extends BasePost {
        // PopularPost에는 추가적인 필드나 메서드가 필요할 경우 이곳에 작성
    }

    // QnaPost는 QnA 게시판의 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "QnA 게시판에 관련된 게시글 ")
    public static class QnaPost extends BasePost {

        @Schema(description = "답변 여부 (QnA 게시판에서만 사용)", example = "true")
        private boolean isAnswered;  // 답변 여부 (QnA 게시판에서만 사용)

        // QnA 게시판에서만 답변 여부를 설정하도록 하는 메서드
        @Schema(description = "QnA 게시판에서만 답변 여부를 설정하는 메서드입니다.")
        public void setAnsweredForQnA(boolean answered) {
            if (this.postType == PostType.QnA) {
                this.isAnswered = answered;
            }
        }

        @Schema(description = "답변 여부를 반환하는 메서드입니다.", example = "true")
        private boolean getIsAnswered() {
            return isAnswered;
        }
    }

    // ExpertLounge는 전문가 라운지 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "전문가 라운지 게시글")
    public static class ExpertLounge extends BasePost {

        @Schema(description = "지역 ID", example = "123")
        private Long areaId; // 지역 ID

        @Schema(description = "위치 ID", example = "456")
        private Long locationId; // 위치 ID
    }

    // ExpertColumn은 전문가 칼럼 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "전문가 칼럼 게시글")
    public static class ExpertColumn extends BasePost {

        @Schema(description = "전체 칼럼 수", example = "10")
        private Long All;  // 전체 칼럼 수

        @Schema(description = "노하우 칼럼 수", example = "5")
        private Long knowHow;  // 노하우 칼럼 수
    }
}
