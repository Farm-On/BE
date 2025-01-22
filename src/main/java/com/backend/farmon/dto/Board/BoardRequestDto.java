package com.backend.farmon.dto.Board;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

public class BoardRequestDto {
    // BasePost는 모든 게시글의 공통적인 속성을 담는 추상 클래스입니다.
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "모든 게시글에 공통된 속성을 가진 기본 게시글 클래스입니다.")
    public static abstract class BasePost {
        // 기존 코드 동일
    }

    // AllPost는 모든 종류의 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @EqualsAndHashCode(callSuper=false)
    @Schema(description = "전체 게시판에 있는 글")
    public static class AllPost extends BasePost {
        // AllPost에는 추가적인 필드나 메서드가 필요할 경우 이곳에 작성
    }

    // PopularPost는 인기 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @EqualsAndHashCode(callSuper=false)
    @Schema(description = "인기 게시판에 있는 글")
    public static class PopularPost extends BasePost {
        // PopularPost에는 추가적인 필드나 메서드가 필요할 경우 이곳에 작성
    }

    //QnaPost는 질문게시판
    @Data
    @EqualsAndHashCode(callSuper=false)
    @Schema(description = "QnA 게시판 게시글")
    public static class QnaPost extends BasePost {
        // 필요한 경우 추가 필드 선언
    }

    //FreePost 자유게시판
    @Data
    @EqualsAndHashCode(callSuper=false)
    @Schema(description = "자유 게시판 게시글")
    public static class FreePost extends BasePost {
        // 필요한 경우 추가 필드 선언
    }

    // ExpertColumn은 전문가 칼럼 게시글을 위한 클래스입니다. (BasePost를 확장)
    @Data
    @EqualsAndHashCode(callSuper=false)
    @Schema(description = "전문가 칼럼 게시글")
    public static class ExpertColumn extends BasePost {
        @Schema(description = "분야 ID", example = "123")
        private Long FilterId; // 지역 ID
    }
}
