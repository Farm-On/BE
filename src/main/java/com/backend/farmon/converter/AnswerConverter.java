package com.backend.farmon.converter;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.domain.Answer;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.Answer.AnswerRequestDTO;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AnswerConverter {
    private UserRepository userRepository;
    private PostRepository postRepository;


    public Answer toEntity(AnswerRequestDTO dto) {
        // 사용자 조회
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 게시글 조회
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // Builder를 사용하여 Answer 생성
        return Answer.builder()
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .post(post)
                .build();
    }
}
