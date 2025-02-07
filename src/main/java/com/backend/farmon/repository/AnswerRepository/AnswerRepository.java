package com.backend.farmon.repository.AnswerRepository;


import com.backend.farmon.domain.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
