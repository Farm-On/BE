package com.backend.farmon.dto.post;

import com.backend.farmon.dto.Answer.AnswerResponseDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostWithAnswersResponseDTO {
    private PostResponseDTO post;
    private List<AnswerResponseDTO> answers;
}
