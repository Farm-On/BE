package com.backend.farmon.dto.Comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class CommenResponseDTO {



    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Schema(description = "부모 댓글 조회 DTO")
    public static class CommentParentReadResponseDto {

        @Schema(description = "댓글 ID", example = "hjunew", required = true)
        private String id;



        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
        private String commentContent;

        @Schema(description = "수정된 시간", example = "2025-01-12T14:30:00", required = true)
        private String modifiedTime;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Schema(description = "대댓글 조회 DTO")
    public static class CommentChildReadResponseDto {

        @Schema(description = "댓글 ID", example = "hjunew", required = true)
        private String id;


        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
        private String commentContent;

        @Schema(description = "수정된 시간", example = "2025-01-12T14:30:00", required = true)
        private String modifiedTime;
    }


    @Getter
    @AllArgsConstructor
    @Builder
    @NoArgsConstructor
    @Schema(description = "부모댓글 + 대댓글 조회 DTO")
    public static class CommentAllByPostResponseDto {

        @Schema(description = "댓글 ID", example = "hjunew", required = true)
        private String id;



        @Schema(description = "댓글 내용", example = "이것은 댓글입니다.", required = true)
        private String commentContent;

        @Schema(description = "수정된 시간", example = "2025-01-12 15:45:00", required = true)
        private String modifiedTime;

        @Schema(description = "대댓글 리스트", example = "[]")
        private List<CommentAllByPostResponseDto> childComments;
    }



    }




