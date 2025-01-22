package com.backend.farmon.strategy.postType;

import com.backend.farmon.dto.post.PostType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 카테고리에 따라 적절한 전략을 반환하는 팩토리 클래스
@RequiredArgsConstructor
@Component
public class PostFetchStrategyFactory {

    private final PopularPostFetchStrategy popularPostFetchStrategy;
    private final AllPostFetchStrategy allPostFetchStrategy;

    public PostFetchStrategy getStrategy(PostType category) {
        switch (category) {
            case POPULAR:
                return popularPostFetchStrategy;
            case ALL:
                return allPostFetchStrategy;
            default:
                throw new IllegalArgumentException("Unsupported category: " + category);
        }
    }
}