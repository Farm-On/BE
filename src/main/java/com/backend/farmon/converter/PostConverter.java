package com.backend.farmon.converter;

import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.PostImg;
import com.backend.farmon.dto.post.PostResponseDTO;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PostConverter {


    public static PostResponseDTO toPostResponseDTO(Post post, List<String> imgs) {
        return PostResponseDTO.builder()
                .postId(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .postLike(post.getLikeCount()) // 좋아요 수
                .postComment(post.getComments().size()) // 댓글 수
                .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))) // 생성일 포맷팅
                .imgUrls(imgs) // 이미지 URL 리스트
                .build();
    }



    }

