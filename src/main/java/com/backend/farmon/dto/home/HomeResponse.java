package com.backend.farmon.dto.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class HomeResponse {
    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 조회 정보")
    public static class HomePageDTO {

        @Schema(description = "커뮤니티 카테고리에 따른 게시글 리스트")
        List <PostDetailDTO> postList;

        @Schema(description = "커뮤니티 카테고리에 따른 게시글 리스트")
        List <PopularPostDetailDTO> popularPostList;

        @Schema(description = "최근 검색어 리스트")
        List <String> recentSearchList;

        @Schema(description = "추천 검색어 리스트")
        List <String> recommendSearchList;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 커뮤니티 게시글 정보")
    public static class PostDetailDTO {

        @Schema(description = "커뮤니티 카테고리", example = "Q&A")
        String category;

        @Schema(description = "커뮤니티 게시글 제목")
        String postTitle;

        @Schema(description = "커뮤니티 게시글 내용")
        String postContent;

        @Schema(description = "게시글 좋아요 횟수", example = "5")
        Integer likeCount;

        @Schema(description = "게시글 댓글 횟수", example = "2")
        Integer commentCount;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "홈 페이지 인기 칼럼 정보")
    public static class PopularPostDetailDTO {

        @Schema(description = "인기 칼럼 제목")
        String popularPostTitle;

        @Schema(description = "인기 칼럼 내용")
        String popularPostContent;

        @Schema(description = "작성자 이름")
        String writer;

        @Schema(description = "작성자 프로필 이미지")
        String profileImage;

        @Schema(description = "인기 칼럼 썸네일 이미지")
        String popularPostImage;
    }
}
