package com.backend.farmon.repository.CommentRepository;

import com.backend.farmon.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    // 게시글과 연관된 댓글 개수 조회
    @Query("SELECT COUNT(c) FROM Comment c WHERE c.post.id = :postId")
    Integer countCommentsByPostId(@Param("postId") Long postId);
}
