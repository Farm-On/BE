package com.backend.farmon.repository.CommentRepository;

import com.backend.farmon.domain.Answer;
import com.backend.farmon.domain.Comment;
import com.backend.farmon.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    // 게시글과 연관된 댓글 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Integer countCommentsByPostId(@Param("postId") Long postId);
    // 게시글에 해당하는 부모 댓글 조회 (parent가 null인 것만)
    @Query("SELECT c FROM Comment c WHERE c.post.id = :postId AND c.parent IS NULL")
    List<Comment> findParentCommentsByPostId(@Param("postId") Long postId);

    @Query("SELECT COUNT(c) FROM Comment c WHERE c.parent.id = :parentId")
    int countByParentId(@Param("parentId") Long parentId);

    @Query("SELECT COALESCE(MAX(c.groupOrder), 0) FROM Comment c WHERE c.groupId = :groupId")
    Optional<Integer> findMaxGroupOrderByGroupId(@Param("groupId") Long groupId);

    // 특정 original_comment_id와 post_id를 가진 댓글이 존재하는지 확인 (대댓글 중복 방지 용도)
    boolean existsByOriginalCommentIdAndPostId(Long originalCommentId, Long postId);

    // originalCommentId를 기준으로 관련 댓글들을 모두 조회합니다.
    List<Comment> findAllByOriginalCommentId(Long originalCommentId);


    List<Comment> findAllByPostId(Long postId);

    List<Comment>findAllByGroupId(Long groupId);

    // 부모댓글이 있는 찾기
    boolean existsByParentId(Long parentId);

    @Query("SELECT MAX(c.groupId) FROM Comment c")
    Optional<Long> findMaxGroupId();

    // 특정 부모 댓글에 대댓글이 있는지 확인하는 메서드 추가
    boolean existsByParentIdAndOriginalCommentId(Long parentId, Long originalCommentId);
}
