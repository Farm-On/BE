package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;

import java.util.List;

public interface PostRepositoryCustom {
    // 커뮤니티 전체 게시글 3개 조회
    public List<Post> findTop3Posts();

    // 커뮤니티 인기 게시글 3개 조회
    public List<Post> findTop3PostsByLikes();
}
