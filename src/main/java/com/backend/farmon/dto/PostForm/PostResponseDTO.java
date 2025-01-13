package com.backend.farmon.dto.PostForm;

import com.backend.farmon.dto.PostType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "게시글 응답 DTO")
public class PostResponseDTO {

    @Schema(description = "게시글 ID", example = "1")
    private Long postid;

    @Schema(description = "게시글 제목", example = "게시글 제목 예시")
    private String postTitle;

    @Schema(description = "게시글 내용", example = "게시글 내용 예시입니다.")
    private String postContent;

    @Schema(description = "게시글 좋아요 수", example = "25")
    private int postlike;

    @Schema(description = "해시태그 내용", example = "작물1")
    private String hash_tag;

    @Schema(description = "게시글 댓글 수", example = "10")
    private int postcomment;

    @Schema(description = "작성 시간", example = "2025-01-01T12:00:00")
    private String createdAt;

    @Schema(description = "수정 시간", example = "2025-01-01T12:30:00")
    private String updatedAt;


     @Schema(description = "게시글 유형", example = "ALL 또는 KNOWHOW")
     @Enumerated(value = EnumType.STRING)
     private PostType postType;

}
