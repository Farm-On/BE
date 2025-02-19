package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.domain.*;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

import static com.backend.farmon.domain.QPost.post;
import static com.backend.farmon.domain.QPostImg.postImg;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final BoardRepository boardRepository;
    private final JPAQueryFactory queryFactory;
    QLikeCount likeCount = QLikeCount.likeCount;
    QPost post = QPost.post;
    QBoard board = QBoard.board;
    QCrop crop = QCrop.crop;
    QBoardPost boardPost= QBoardPost.boardPost;

    // 전체 게시글 3개 조회
    @Override
    public List<Post> findTopPosts(Integer limit) {
        QPost originalPost = new QPost("originalPost");

        return queryFactory.selectFrom(originalPost)
                .where(originalPost.id.in(
                        JPAExpressions.select(post.originalPostId) // 현재 게시글의 원본 ID를 가져옴
                                .from(post)
                                .where(post.board.postType.eq(PostType.ALL)
                                        .and(post.originalPostId.isNotNull())) // original_post_id가 있는 경우만 조회
                ))
                .orderBy(originalPost.createdAt.desc()) // 원본 게시글을 기준으로 정렬
                .limit(limit)
                .fetch();
    }

    // 인기 게시글 3개 조회
    @Override
    public List<Post> findTopPostsByLikes(Integer limit) {
        QPost originalPost = new QPost("originalPost");

        return queryFactory.selectFrom(originalPost)
                .leftJoin(originalPost.postlikes, likeCount).fetchJoin()
                .where(originalPost.id.in(
                        JPAExpressions.select(post.originalPostId) // 현재 게시글의 원본 ID를 가져옴
                                .from(post)
                                .where(post.board.postType.eq(PostType.POPULAR)
                                        .and(post.originalPostId.isNotNull())) // original_post_id가 있는 경우만 조회
                ))
                .groupBy(originalPost)
                .orderBy(likeCount.count().desc(), originalPost.createdAt.desc())
                .limit(limit)
                .fetch();
    }

    // 게시판 타입별로 조회 (전문가 칼럼, Q&A)
    @Override
    public List<Post> findTopPostsByPostTYpe(PostType postType, Integer limit) {
        return queryFactory.select(post)
                .from(post)
                .join(post.board, board).fetchJoin() // Post와 Board를 조인
                .leftJoin(post.postlikes, likeCount) // Post와 LikeCount를 조인
                .where(board.postType.eq(postType)) // PostType으로 필터링
                .groupBy(post) // Post별로 그룹화
                .orderBy(
                        likeCount.count().desc(), // 좋아요 개수로 정렬
                        post.createdAt.desc() // 최신순 정렬
                )
                .limit(limit) // 3개 제한
                .fetch();
    }


    // 인기 전문가 칼럼 6개 조회
    @Override
    public List<Post> findTop6ExpertColumnPostsByPostId(List<Long> popularPostsIdList) {
        return queryFactory.select(post)
                .from(post)
                .join(post.board, board).fetchJoin()
                .leftJoin(post.postlikes, likeCount)
                .where(
                        board.postType.eq(PostType.EXPERT_COLUMN) // 전문가 칼럼 조건
                                .and(
                                        popularPostsIdList != null && !popularPostsIdList.isEmpty()
                                                ? post.id.in(popularPostsIdList).or(post.id.notIn(popularPostsIdList))
                                                : null
                                )
                )
                .groupBy(post)
                .orderBy(
                        // 인기 게시글 우선 정렬
                        popularPostsIdList != null && !popularPostsIdList.isEmpty()
                                ? Expressions.stringTemplate("CASE WHEN {0} IN ({1}) THEN 1 ELSE 2 END", post.id, Expressions.constant(popularPostsIdList)).asc()
                                : null,
                        // popularPostsIdList 내부 정렬
                        popularPostsIdList != null && !popularPostsIdList.isEmpty()
                                ? Expressions.stringTemplate("FIELD({0}, {1})", post.id, Expressions.constant(popularPostsIdList)).asc()
                                : null,
                        likeCount.count().desc(), // 좋아요 개수 내림차순
                        post.createdAt.desc() // 작성일 내림차순
                )
                .limit(6) // 6개 제한
                .fetch();
    }

    @Override
    public Page<Post> findAllByBoardId(Long boardId, Pageable pageable) {
        QPost post = QPost.post;
        QPostImg postImg = QPostImg.postImg;
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // 게시판 ID로 게시글 및 관련 이미지 조회
        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.postImgs, postImg).fetchJoin() // Post와 PostImg를 Join
                .where(post.board.id.eq(boardId)) // 게시판 ID로 필터링
                .orderBy(post.createdAt.desc()) // 최신순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 게시글 수 조회
        long total = queryFactory
                .select(post.id) // countQuery 최적화 (ID만 선택)
                .from(post)
                .where(post.board.id.eq(boardId))
                .fetchCount();

        return new PageImpl<>(posts, pageable, total);
    }

@Override
public Page<Post> findPostsByBoardIdAndCrops(Long boardId, List<String> cropNames, Pageable pageable) {
    Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

    BooleanBuilder whereClause = new BooleanBuilder();
    whereClause.and(post.board.id.eq(boardId));

    if (cropNames != null && !cropNames.isEmpty()) {
        // 1️⃣ cropNames를 기준으로 Crop ID 목록 가져오기
        List<Long> cropIds = queryFactory
                .select(crop.id)
                .from(crop)
                .where(crop.name.in(cropNames)) // cropNames 리스트로 검색
                .fetch();

        log.info("필터링할 cropIds: " + cropIds);

        // 2️⃣ 가져온 Crop ID 목록으로 Post 필터링
        if (!cropIds.isEmpty()) {
            whereClause.and(post.crop.id.in(cropIds));
        } else {
            // 일치하는 Crop이 없으면 결과가 없음
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }
    }

    // 3️⃣ 필터링된 게시글 조회
    List<Post> posts = queryFactory.selectFrom(post)
            .leftJoin(post.postImgs, postImg).fetchJoin()  // 게시글 이미지
            .leftJoin(post.crop, crop)  // Post와 Crop 관계 조인
            .where(whereClause)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

    log.info("필터링된 게시글 개수: " + posts.size());

    // 4️⃣ 전체 게시글 개수 조회
    long totalCount = queryFactory.selectFrom(post)
            .leftJoin(post.crop, crop)
            .where(whereClause)
            .fetchCount();

    return new PageImpl<>(posts, pageable, totalCount);
}





    @Override
    public Page<Post> findPopularPosts(Long boardId, Pageable pageable) {
        QPost post = QPost.post;
        QPostImg postImg = QPostImg.postImg;
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));
        // 게시판별 인기 게시글 및 관련 이미지 조회 (좋아요 수 기준 정렬)
        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.postImgs, postImg).fetchJoin() // Post와 PostImg를 Join
                .where(post.board.id.eq(boardId)) // 게시판 ID로 필터링
                .orderBy(post.postLikes.desc()) // 좋아요 수 기준 내림차순 정렬
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // 전체 게시글 수 조회 (countQuery로 분리하여 성능 최적화)
        long total = queryFactory
                .select(post.id) // countQuery를 최적화하기 위해 ID만 선택
                .from(post)
                .where(post.board.id.eq(boardId))
                .fetchCount();

        return new PageImpl<>(posts, pageable, total);
    }



}
