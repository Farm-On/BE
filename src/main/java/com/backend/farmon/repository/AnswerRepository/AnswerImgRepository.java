package com.backend.farmon.repository.AnswerRepository;

import com.backend.farmon.domain.AnswerImg;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import java.util.List;

public interface AnswerImgRepository extends JpaRepository<AnswerImg, Long> {
    List<AnswerImg> findAllByAnswerId(Long answerId);
=======
public interface AnswerImgRepository extends JpaRepository<AnswerImg, Long> {
>>>>>>> origin/develop
}
