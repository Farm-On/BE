package com.backend.farmon.service.CommentService;

import com.backend.farmon.dto.Comment.CommentRequestDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;
import org.springframework.transaction.annotation.Transactional;

public interface CommentService  {


    @Transactional
    CommentResponseDTO saveComment(Long postId, Long parentId, CommentRequestDTO.CommentSaveRequestDto dto);

    @Transactional
    CommentResponseDTO updateComment(Long commentId, CommentRequestDTO.CommentUpdateRequestDto dto);

    @Transactional
    void deleteComment(Long commentId);
}
