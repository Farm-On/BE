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

<<<<<<< HEAD
    void deleteByOriginalPostId(Long originalPostId);
=======

>>>>>>> origin/develop

    Page<Post> findAllByUserId(Long userId, Pageable pageable);

    List<Post> findAllByUserId(Long userId);

    List<Post> findByUserIdIn(List<Long> userIds);

    @Query("SELECT COUNT(l) FROM LikeCount l WHERE l.post.id = :postId")
    int getLikeCount(@Param("postId") Long postId);


    // PostRepository에 메서드 추가
    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.id = :postId")
    Optional<Post> findByIdWithComments(@Param("postId") Long postId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.comments WHERE p.originalPostId = :originalPostId")
    List<Post> findByOriginalPostIdWithComments(@Param("originalPostId") Long originalPostId);

    List<Post> findAllByOriginalPostId(Long originalPostId);

}
