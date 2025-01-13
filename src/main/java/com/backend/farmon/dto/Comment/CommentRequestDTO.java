package com.backend.farmon.dto.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class CommentRequestDTO {




//-------------댓글 -----------------

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "부모 댓글 저장 DTO")
    public class CommentSaveRequestDto {

        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
        private String commentContent;

        @Schema(description = "작성자 유저 번호", example = "1", required = true)
        private Long userNo;

        @Schema(description = "게시글 번호", example = "10", required = true)
        private Long postNo;


    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "대댓글 조회 DTO")
    public class CommentParenReadtDTO {

        @Schema(name="대댓글을 특정 조회순으로 가져옴")
        private String sort;

    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "부모 댓글 조회 DTO")
    public class CommentChilldtDTO {

        @Schema(name="부모 댓글을 특정 조회순으로 가져옴")
        private String sort;

    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "대댓글 저장 DTO")
    public class CommentSaveChildRequestDto {

        @Schema(description = "대댓글 저장 내용", required = true, example = "This is a child comment")
        private String commentContent;

        @Schema(description = "댓글을 저장하는 회원 번호", example = "1234")
        private Long userNo;

        @Schema(description = "댓글 깊이", example = "2")
        private int depth;
    }



    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글  수정 DTO")
    public class CommentUpdateRequestDto {

        @Schema(description = "업데이트할 댓글 내용", example = "수정된 댓글 내용입니다.")
        private String commentContent;

      //시간 추가?

    }



    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "댓글 페이징 요청 DTO")
    public class CommentPagingRequestDto {

        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
        private String commentContent;

        @Schema(description = "작성자 유저 번호", example = "1", required = true)
        private Long userNo;

        @Schema(description = "게시글 번호", example = "10", required = true)
        private Long postNo;
    }


}
