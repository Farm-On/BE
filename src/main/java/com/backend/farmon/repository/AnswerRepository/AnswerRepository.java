package com.backend.farmon.repository.AnswerRepository;


import com.backend.farmon.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

<<<<<<< HEAD
import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByPostId(Long postId);
=======
public interface AnswerRepository extends JpaRepository<Answer, Long> {
>>>>>>> origin/develop
}
