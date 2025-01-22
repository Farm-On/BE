package com.backend.farmon.service.PostService;

import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostType;

public interface PostQueryService {

    // 홈 화면 카테고리에 따른 커뮤니티 게시글 3개씩 조회
    HomeResponse.PostListDTO findHomePostsByCategory(PostType category);

}
