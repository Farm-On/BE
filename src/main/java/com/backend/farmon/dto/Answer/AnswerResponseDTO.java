package com.backend.farmon.dto.Answer;


import com.backend.farmon.domain.Answer;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.commons.DateFormatUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(description = "답변 요청 DTO")
public class AnswerResponseDTO {

    @Schema(description = "답변 제목")
    private String title;

    @Schema(description = "답변 내용")
    private String content;

    @Schema(description = "답변자 ID")
    private Long answeredUserId; // 답변한 사람의 ID 추가

    @Schema(description = "답변자가 올린 답글 사진")
    private List<String> imgUrl;

    @Schema(description = "작성 시간")
    private String createdAt;

    @Builder
    public AnswerResponseDTO(Answer answer, List<String> imgUrls){
        this.title = answer.getTitle();
        this.content = answer.getContent();
        this.answeredUserId=answer.getId();
        this.createdAt = DateFormatUtil.formatDate(answer.getCreatedAt()); // 날짜 포맷 적용
        this.imgUrl = imgUrls;
    }


}
