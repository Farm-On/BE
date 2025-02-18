package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

    // 원본 게시글 ID를 기준으로 삭제
    void deleteByOriginalPostId(Long originalPostId);

    // 사용자 ID에 해당하는 모든 게시글 페이징 조회
    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    // 사용자 ID에 해당하는 모든 게시글 조회
    List<Post> findAllByUserId(Long userId);

    // 사용자 ID 목록에 해당하는 게시글 조회
    List<Post> findByUserIdIn(List<Long> userIds);

    // 게시글에 대한 좋아요 수 조회
    @Query("SELECT COUNT(l) FROM LikeCount l WHERE l.post.id = :postId")
    int getLikeCount(@Param("postId") Long postId);

    // 댓글을 포함한 게시글 조회 (ID 기준)
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :postId")
    Optional<Post> findByIdWithComments(@Param("postId") Long postId);

    // 댓글을 포함한 게시글 조회 (원본 게시글 ID 기준)
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.originalPostId = :originalPostId")
    List<Post> findByOriginalPostIdWithComments(@Param("originalPostId") Long originalPostId);

    // 원본 게시글 ID에 해당하는 모든 게시글 조회
    List<Post> findAllByOriginalPostId(Long originalPostId);
}
