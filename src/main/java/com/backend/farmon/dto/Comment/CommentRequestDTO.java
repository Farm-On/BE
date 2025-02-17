package com.backend.farmon.dto.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

public class CommentRequestDTO {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 저장 DTO")
    public static class CommentSaveRequestDto {

        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = false)
        @NotBlank(message = "댓글 내용은 필수입니다.")
        private String commentContent;

        @Schema(description = "작성자 유저 아이디", example = "1", required = true)
        @NotNull(message = "유저 아이디는 필수입니다.")
        private Long userId;

        @Schema(description = "게시글 아이디", example = "1", required = true)
        @NotNull(message = "게시글 아이디는 필수입니다.")
        private Long postId;

        @Schema(description = "부모 댓글 ID (대댓글인 경우). 최상위 댓글은 null", example = "null", required = false)
        private Long parentId;



    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 수정 DTO")
    public static class CommentUpdateRequestDto {



        @Schema(description = "수정할 댓글 내용", example = "수정된 댓글 내용입니다.", required = true)
        @NotBlank(message = "수정할 댓글 내용은 필수입니다.")
        private String commentContent;

        @Schema(description = "작성자 유저 아이디", example = "1", required = true)
        @NotNull(message = "유저 아이디는 필수입니다.")
        private Long userId;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 삭제 DTO")
    public static class CommentDeleteRequestDto {
        @Schema(description = "게시글 아이디", example = "1", required = true)
        @NotNull(message = "게시글 아이디는 필수입니다.")
        private Long postId;

        @Schema(description = "삭제할 댓글 ID", example = "1", required = true)
        @NotNull(message = "댓글 ID는 필수입니다.")
        private Long commentId;

        @Schema(description = "작성자 유저 아이디", example = "1", required = true)
        @NotNull(message = "유저 아이디는 필수입니다.")
        private Long userId;
    }

}
