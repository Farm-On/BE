package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.QLikeCount;
import com.backend.farmon.domain.QPost;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QPost post = QPost.post;
    QLikeCount likeCount = QLikeCount.likeCount;

    // 커뮤니티 전체 게시글 3개 조회
    @Override
    public List<Post> findTop3Posts() {
        return queryFactory.selectFrom(post)
                .orderBy(post.createdAt.desc())
                .limit(3)
                .fetch();
    }

    // 커뮤니티 인기 게시글 3개 조회
    @Override
    public List<Post> findTop3PostsByLikes() {
        return queryFactory.selectFrom(post)
                .leftJoin(post.postlikes, likeCount)
                .groupBy(post)
                .orderBy(likeCount.count().desc(), post.createdAt.desc())
                .limit(3)
                .fetch();
    }
}
