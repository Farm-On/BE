package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.post.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepositoryCustom {
    // 커뮤니티 전체 게시글 3개 조회
    public List<Post> findTop3Posts();

    // 커뮤니티 인기 게시글 3개 조회
    public List<Post> findTop3PostsByLikes();

    // 커뮤니티 카테고리별 게시글 3개 조회
    public List<Post> findTop3PostsByPostTYpe(PostType postType);

    // 인기 전문가 칼럼 6개 조회
    public List<Post> findTop6ExpertColumnPostsByPostId(List<Long> popularPostsIdList);

    Page<Post> findAllByBoardId(Long boardId, Pageable pageable);


    Page<Post> findPostsByBoardIdAndCrops(Long boardId, List<String> cropNames, Pageable pageable);


    Page<Post> findPopularPosts(@Param("boardId") Long boardId, Pageable pageable);

}
