package com.backend.farmon.dto.Board;

import com.backend.farmon.dto.post.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class BoardRequestDto {

    /**
     * 공통 필드 추출: 모든 게시글 DTO에서 공통으로 사용되는 필드를 `BasePost`로 정의합니다.
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "게시글의 기본 정보")
    public static class BasePost {
        @Schema(description = "게시글 제목", example = "농촌에서 살아남기")
        @NotBlank
        private String postTitle; // 게시글 제목

        @Schema(description = "게시글 내용", example = "쌀을 기르는 법")
        @NotBlank
        private String postContent; // 게시글 내용

        @Schema(description = "사용자 ID", example = "1")
        @NotNull
        private Long userId; // 사용자 ID

        @Schema(description = "게시판 ID", example = "1")
        @NotNull
        private Long boardId; // 게시판 ID

        @Schema(description = "게시글에 대한 댓글 수", example = "0")
        private int comment = 0; // 댓글 수, 기본값 0

        @Schema(description = "게시글 종류 (예: QnA, 일반 게시글 등)", example = "QNA")
        @NotNull
        private PostType postType; // 게시글 종류

       //private List<MultipartFile> imgList; // 이미지 리스트 (Base64 또는 URL)
        // Json으로 처리 불가

        @Schema(
                description = "이미지 리스트 (Base64 인코딩, 'data:image/png;base64,' 프리픽스가 없는 순수 Base64 문자열만 허용)",
                example = "[\"iVBORw0KGgoAAAANSUhEUgAA...\"]"
        )
        private List<String> imgList; // Base64 문자열 리스트

    }

    /**
     * 자유 게시판 DTO: `BasePost`를 확장하여 자유 게시판 전용 필드가 필요한 경우 추가합니다.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @Schema(description = "자유 게시판 게시글")
    public static class FreePost extends BasePost {
        // 자유 게시판 전용 필드가 필요하다면 여기에 추가
    }

    /**
     * QnA 게시판 DTO: `BasePost`를 확장하고, 카테고리 관련 필드를 추가합니다.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @Schema(description = "QnA 게시판 게시글")
    public static class QnaPost extends BasePost {
        @Schema(description = "상위 분야 카테고리", example = "곡물")
        private String Categorytitle;

        @Schema(description = "하위 분야 카테고리", example = "쌀")
        @NotBlank
        private String crop;
    }

    /**
     * 인기 게시판 DTO: `BasePost`를 확장하여 인기 게시판 전용 필드가 필요한 경우 추가합니다.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @Schema(description = "인기 게시판 게시글")
    public static class PopularPost extends BasePost {
        // 인기 게시판 전용 필드가 필요하다면 여기에 추가
    }

    /**
     * 전문가 칼럼 DTO: `BasePost`를 확장하고, 카테고리 관련 필드를 추가합니다.
     */
    @Data
    @EqualsAndHashCode(callSuper = true)
    @Schema(description = "전문가 칼럼 게시글")
    public static class ExpertColumn extends BasePost {

        @Schema(description = "상위 분야 카테고리", example = "옥수수")
        private String  Categorytitle;

        @Schema(description = "하위 분야 카테고리", example = "쌀")
        private String crop;
    }
}
