package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.Comment.CommentRequestDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;
import com.backend.farmon.service.CommentService.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "댓글 ", description = "댓글에 관한 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    /**
     * 댓글 저장 (최상위 댓글 또는 대댓글)
     */
    @Operation(summary = "댓글 저장", description = "게시글에 최상위 댓글을 저장합니다.")
    @PostMapping("/{postId}/comments")
    public ApiResponse<CommentResponseDTO> saveComment(
            @RequestBody CommentRequestDTO.CommentSaveRequestDto dto) {
        CommentResponseDTO savedComment = commentService.saveComment(dto.getPostId(), dto.getParentId(), dto);
        return ApiResponse.onSuccess(savedComment);
    }


    @Operation(summary = "대댓글 저장", description = "게시글에 대댓글을 저장합니다.")
    @PostMapping("/{postId}/comments/{parentId}/replies")
    public ApiResponse<CommentResponseDTO> saveReply(
            @RequestBody CommentRequestDTO.CommentSaveRequestDto dto) {
        CommentResponseDTO savedReply = commentService.saveComment(dto.getPostId(), dto.getParentId(), dto);
        return ApiResponse.onSuccess(savedReply);
    }


    /**
     * 댓글 삭제 (논리적 삭제)
     */
    @Operation(summary = "댓글 삭제", description = "댓글을 논리적으로 삭제합니다.")
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ApiResponse<Void> deleteComment(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ApiResponse.onSuccess(null); // 성공 응답만 반환
    }




}
