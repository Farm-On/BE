package com.backend.farmon.dto.PostForm;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostPagingRequestDto {

    private int page;   // 페이지 번호
    private int size;   // 페이지 크기
    private String sort; // 정렬 기준
}
