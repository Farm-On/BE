package com.backend.farmon.strategy.postType;

import com.backend.farmon.domain.Post;

import java.util.List;

public interface PostFetchStrategy {
    List<Post> fetchPosts();
}
