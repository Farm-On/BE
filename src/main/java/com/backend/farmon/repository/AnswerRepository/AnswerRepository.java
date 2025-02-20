package com.backend.farmon.repository.AnswerRepository;

import com.backend.farmon.domain.Answer;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 특정 게시글의 모든 답변 조회
    List<Answer> findAllByPostId(Long postId);

    // 원본 게시글 ID로 관련된 모든 답변 조회 (연관된 게시글 포함)
    List<Answer> findAllByPostOriginalPostId(Long originalPostId);

    Optional<Answer> findByPostAndUser(Post post, User user);
}
