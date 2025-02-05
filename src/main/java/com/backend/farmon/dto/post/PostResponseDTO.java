package com.backend.farmon.dto.post;

import com.backend.farmon.domain.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시글 내용 응답 DTO")
public class PostResponseDTO {

        @Schema(description = "게시글 ID", example = "1")
        private Long postId;

        @Schema(description = "게시글 제목", example = "게시글 제목 예시")
        private String postTitle;

        @Schema(description = "게시글 내용 ", example = "게시글 내용 ")
        private String postContent;

        @Schema(description = "게시글 좋아요 수", example = "25")
        private int postLike;

        @Schema(description = "게시글 댓글 수", example = "10")
        private int postComment;

        @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
        private String createdAt;

//        @Schema(description = "게시글이 속한 게시판 유형", example = "ALL 또는 KNOWHOW")
//        private String postType;

        @Schema(description = "게시글 사진", example = "img")
        private List<String> imgUrls;


        @Builder
        public PostResponseDTO(Post post, List<String> imgUrls,String timeAgo) {
                this.postId = post.getId();
                this.postTitle = post.getPostTitle();
                this.postContent = post.getPostContent();
                this.postLike = post.getPostLikes();
                this.createdAt =timeAgo;
               // this.postType = String.valueOf(post.getPostType());
                this.imgUrls = imgUrls;
        }

}
