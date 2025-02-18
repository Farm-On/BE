package com.backend.farmon.dto.post;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.Answer.AnswerResponseDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@Schema(description = "게시글 내용 응답 DTO")
public class PostResponseDTO {

        @Schema(description = "게시글 ID", example = "1")
        private Long postId;

        @Schema(description = "게시글 제목", example = "게시글 제목 예시")
        private String postTitle;

        @Schema(description = "게시글 소제목", example = "게시글 제목 예시")
        private String subTitle;

        @Schema(description = "게시글 내용", example = "게시글 내용 예시")
        private String postContent;

        @Schema(description = "게시글 좋아요 수", example = "25")
        private int postLike;

        @Schema(description = "게시글 댓글 수", example = "10")
        private int postComment;

        @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
        private String createdAt;

        @Schema(description="질문 상위 분야",example = "사과")
        private String Category;

        @Schema(description="질문 하위 분야",example = "과일")
        private String subCategory;

        @Schema(description = "게시글 이미지 URL 리스트", example = "[\"https://example.com/image1.jpg\", \"https://example.com/image2.jpg\"]")
        private List<String> imageUrls; // 게시글 이미지 URL 리스트

        @Schema(description = "답변 리스트", example = "[{...}, {...}]")
        private List<AnswerResponseDTO> answers; // 답변 리스트


        private List<CommentResponseDTO> comments; // 댓글 리스트

        public PostResponseDTO(Post post, List<String> imgUrls, String timeAgo, List<CommentResponseDTO> comments) {
                this.postId = post.getId();
                this.postTitle = post.getPostTitle();
                this.postContent = post.getPostContent();
                this.createdAt=timeAgo;
                this.imageUrls = imgUrls;
                this.comments = comments;
        }
        /**
         * 기본 생성자: answers 필드를 빈 리스트로 초기화
         */
        public PostResponseDTO() {
                this.answers = new ArrayList<>(); // 답변이 없으면 빈 리스트로 초기화
                this.imageUrls = new ArrayList<>(); // 이미지도 없으면 빈 리스트로 초기화
        }

        /**
         * 생성자: 게시글 정보, 이미지 URL 리스트, 답변 리스트, 작성 시간으로 DTO 생성
         */
        @Builder
        public PostResponseDTO(Post post, List<String> imageUrls, List<AnswerResponseDTO> answers, String timeAgo) {
                this.postId = post.getId();
                this.postTitle = post.getPostTitle();
                this.postContent = post.getPostContent();
                this.postLike = post.getPostLikes();
                this.createdAt = timeAgo;
                this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>(); // null 방지 처리
                this.answers = answers != null ? answers : new ArrayList<>(); // null 방지 처리
        }


        @Builder
        public PostResponseDTO(Post post, List<String> imageUrls, String timeAgo) {
                this.postId = post.getId();
                this.postTitle = post.getPostTitle();
                this.postContent = post.getPostContent();
                this.postLike = post.getPostLikes();
                this.createdAt = timeAgo;
                this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>(); // null 방지 처리
        }
}
