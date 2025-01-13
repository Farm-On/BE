package com.backend.farmon.dto.PostForm;

import com.backend.farmon.dto.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class PostRequestDTO {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "게시글 저장 DTO")
    public class PostSaveRequestDto {
        @Schema(description = "게시글 제목", required = true)
        @NotEmpty(message = "제목을 입력해주세요.")
        private String postTitle;

        @Schema(description = "게시글 내용", required = true)
        @NotEmpty(message = "내용을 입력해주세요.")
        private String postContent;

        @Schema(description = "좋아요 수", defaultValue = "0")
        private int postLike = 0;

        // 여기는 확신 못함
        @Schema(description = "해시태그")
        private String hashTag;

        @Schema(description = "조회수", defaultValue = "0")
        private int views=0;

        @Schema(description = "게시판 5개")
        @Enumerated(value = EnumType.STRING)
        private PostType postType;



        @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
        private String createdAt;

        @Schema(description = "수정 시간", example = "2025-01-01T12:30:00")
        private String updatedAt;

    }


}


