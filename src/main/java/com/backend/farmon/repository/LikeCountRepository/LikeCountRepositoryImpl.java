package com.backend.farmon.repository.LikeCountRepository;

import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;

public class LikeCountRepositoryImpl implements LikeCountRepositoryCustom{
    @Override
    public boolean checkLike(Post post, User user) {
        return false;
    }

    @Override
    public void like(Post post, User user) {

    }

    @Override
    public void unlike(Post post, User user) {

    }
}
