package com.backend.farmon.dto.post;

import com.backend.farmon.domain.PostImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "이미지 업로드 응답 DTO")
public class ImgResponseDTO {

    @Schema(description = "이미지 ID", example = "1")
    private Long id;

    @Schema(description = "업로드된 원본 파일 이름", example = "example.jpg")
    private String originalFileName;

    @Schema(description = "저장된 파일 이름", example = "abcd1234.jpg")
    private String storedFileName;

    @Builder
    public ImgResponseDTO(List<PostImg> imgs){
        for (PostImg img : imgs){
            this.id = img.getId();
            this.originalFileName = img.getOriginalFileName();
            this.storedFileName = img.getStoredFileName();
        }
    }

    @Builder
    public ImgResponseDTO(PostImg img){
        this.id = img.getId();
        this.originalFileName = img.getOriginalFileName();
        this.storedFileName = img.getStoredFileName();
    }

}