package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {



    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    List<Post> findAllByUserId(Long userId);

    List<Post> findByUserIdIn(List<Long> userIds);

    @Query("SELECT COUNT(l) FROM LikeCount l WHERE l.post.id = :postId")
    int getLikeCount(@Param("postId") Long postId);




}