package com.backend.farmon.repository.CommentRepository;

import com.backend.farmon.domain.Comment;
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


    boolean existsByParentId(Long parentId);


}
