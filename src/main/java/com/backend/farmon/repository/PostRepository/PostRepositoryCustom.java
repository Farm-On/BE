package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.post.PostType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepositoryCustom {
    // 커뮤니티 전체 게시글 3개 조회
    List<Post> findTopPosts(Integer limit);

    // 커뮤니티 인기 게시글 3개 조회
    List<Post> findTopPostsByLikes(Integer limit);

    // 커뮤니티 카테고리별(QNA, 전문가, 자유게시판) 게시글 3개 조회
    List<Post> findTopPostsByPostTYpe(PostType postType, Integer limit);

    // 인기 전문가 칼럼 6개 조회
    List<Post> findTop6ExpertColumnPostsByPostId(List<Long> popularPostsIdList);

    // 필터링없이 조회 
    Page<Post> findAllByBoardId(Long boardId, Pageable pageable);

    //작물로 필터링
    Page<Post> findPostsByBoardIdAndCrops(Long boardId, List<String> cropNames, Pageable pageable);

    //인기게시판용 좋아요 순으로 정렬 
    Page<Post> findPopularPosts(@Param("boardId") Long boardId, Pageable pageable);

    // 검색기능 
    Page<Post> findPostsBySearchQuery(String searchQuery, Long boardId, Pageable pageable);
}
