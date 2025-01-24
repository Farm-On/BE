package com.backend.farmon.repository.LikeCountRepository;

import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;

public interface LikeCountRepositoryCustom {

    boolean checkLike(Post post, User user);
    void like(Post post, User user);
    void unlike(Post post, User user);

}
