package com.backend.farmon.repository.LikeCountRepository;

import com.backend.farmon.domain.LikeCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeCountRepository extends JpaRepository<LikeCount, Long>, LikeCountRepositoryCustom {

    // 게시글과 연관된 좋아요 개수 조회
    @Query("SELECT COUNT(lc) FROM LikeCount lc WHERE lc.post.id = :postId")
    Integer countLikeCountsByPostId(@Param("postId") Long postId);
}
