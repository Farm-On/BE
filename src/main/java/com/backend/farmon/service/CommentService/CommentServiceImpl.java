package com.backend.farmon.service.CommentService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.controller.UserController;
import com.backend.farmon.domain.Comment;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.Comment.CommentRequestDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.backend.farmon.repository.CommentRepository.CommentRepository;
import com.backend.farmon.repository.CommentRepository.CommentRepositoryImpl;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserController userController;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    /**
     * 댓글 저장 (최상위 댓글 / 대댓글)
     */
    @Transactional
    @Override
    public CommentResponseDTO saveComment(Long postId, Long parentId, CommentRequestDTO.CommentSaveRequestDto dto) {

        // 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다."));

        if (post.getBoard().getPostType() == PostType.QNA) {
            throw new GeneralException(ErrorStatus.BOARD_TYPE_NOT_COMMENTED);
        }

        // 사용자 조회
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자의 아이디가 없습니다."));

        // 부모 댓글 처리 (parentId가 null이면 최상위 댓글로 간주)
        Comment parent = null;
        int depth = 0;
        Long groupId = null;
        int groupOrder = 0;

        if (parentId != null) { // 대댓글 처리
            parent = commentRepository.findById(parentId)
                    .orElseThrow(() -> new IllegalArgumentException("부모 댓글이 존재하지 않습니다."));
            depth = parent.getDepth() + 1; // 부모 댓글의 깊이 + 1
            groupId = parent.getGroupId(); // 부모 댓글의 그룹 ID 사용

            // 삭제된 부모 댓글에는 대댓글 작성 불가
            if (parent.getIsDeleted()) {
                throw new IllegalArgumentException("삭제된 댓글에는 대댓글을 작성할 수 없습니다.");
            }

            // 부모 댓글에 이미 대댓글이 있는지 확인
            boolean hasChildComment = commentRepository.existsByParentId(parent.getId());
            if (hasChildComment) {
                throw new IllegalArgumentException("부모 댓글에 이미 대댓글이 존재합니다. 대댓글은 하나만 작성할 수 있습니다.");
            }

            // 그룹 내에서 가장 큰 groupOrder 값을 가져와 +1
            groupOrder = commentRepository.findMaxGroupOrderByGroupId(groupId)
                    .orElse(0) + 1; // 값이 없으면 기본값 0으로 시작
        }

        // 댓글 생성
        Comment comment = Comment.builder()
                .content(dto.getCommentContent())
                .authorName(user.getUserName())  // User 엔티티에서 이름 추출
                .user(user)
                .post(post)
                .parent(parent)
                .depth(depth)
                .groupId(groupId) // 최상위 댓글은 null, 대댓글은 부모의 groupId 사용
                .groupOrder(groupOrder)
                .isDeleted(false)
                .build();

        log.info("댓글 생성 시작");
        Comment savedComment = commentRepository.save(comment);
        log.info("댓글 저장 완료: {}", savedComment);

        // 최상위 댓글인 경우 자기 자신의 ID를 groupId로 설정하고 저장
        if (savedComment.getParent() == null) {
            savedComment.setGroupId(savedComment.getId());
            commentRepository.save(savedComment); // groupId 업데이트를 반영하기 위해 다시 저장
            log.info("최상위 댓글 groupId 설정 완료: {}", savedComment.getGroupId());
        }

        return new CommentResponseDTO(savedComment);
    }



    /**
     * 댓글 수정 (내용만 수정 가능)
     */
    @Transactional
    @Override
    public CommentResponseDTO updateComment(Long commentId, CommentRequestDTO.CommentUpdateRequestDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalStateException("삭제된 댓글은 수정할 수 없습니다."));

        if (!comment.getIsDeleted()) {
            comment.setContent(dto.getCommentContent());
        } else {
            throw new IllegalStateException("삭제된 댓글은 수정할 수 없습니다.");
        }

        // CommentResponseDTO로 변환하여 반환
        return new CommentResponseDTO(comment);
    }


    /**
     * 댓글 삭제 (부모 댓글은 isDeleted = true 처리)
     * 대댓글은 직접 삭제
     */
    @Transactional
    @Override
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (comment.getParent() == null) {
            // 부모 댓글 삭제 시, 자식 댓글이 있으면 논리 삭제 처리
            comment.setIsDeleted(true);
            // comment.getChildren().forEach(child -> child.setIsDeleted(true));
        } else {
            // 대댓글(답글)은 직접 삭제
            commentRepository.delete(comment);
        }
    }
}
