package com.backend.farmon.dto.Comment;

import com.backend.farmon.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 댓글 조회 응답 DTO
 */
@Getter
@Setter
public class CommentResponseDTO {

    private Long id; // 댓글 ID
    private String content; // 댓글 내용
    private String authorName; // 작성자 이름
    private Long parentId; // 부모 댓글 ID (없으면 null)
    private Long groupId; // 그룹 ID
    private Integer groupOrder; // 그룹 내 순서
    private Integer depth; // 댓글 깊이
    private Boolean isDeleted; // 삭제 여부
    private LocalDateTime createdAt; // 생성 시간
    private int commentCount; // 댓글 수 추가
    private List<CommentResponseDTO> children; // 대댓글 리스트

    // Entity -> DTO 변환
    public CommentResponseDTO(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorName = comment.getAuthorName();
        this.parentId = (comment.getParent() != null) ? comment.getParent().getId() : null;
        this.groupId = comment.getGroupId();
        this.groupOrder = comment.getGroupOrder();
        this.depth = comment.getDepth();
        this.isDeleted = comment.getIsDeleted();
        this.createdAt = comment.getCreatedAt();
        // Null 체크 후 처리
        this.children = (comment.getChildren() != null)
                ? comment.getChildren().stream()
                .map(CommentResponseDTO::new)
                .collect(Collectors.toList())
                : new ArrayList<>();
    }
}