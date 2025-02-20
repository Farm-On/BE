package com.backend.farmon.repository.AnswerRepository;

import com.backend.farmon.domain.AnswerImg;
import com.backend.farmon.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface AnswerImgRepository extends JpaRepository<AnswerImg, Long> {

    // 특정 답변에 연결된 이미지 조회
    List<AnswerImg> findAllByAnswerId(Long answerId);

    // 특정 답변에 대한 이미지를 삭제
    void deleteAllByAnswerId(Long answerId);

}
