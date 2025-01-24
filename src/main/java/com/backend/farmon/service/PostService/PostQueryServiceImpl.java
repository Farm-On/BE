package com.backend.farmon.service.PostService;

import com.backend.farmon.converter.HomeConverter;
import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.CommentRepository.CommentRepository;
import com.backend.farmon.repository.LikeCountRepository.LikeCountRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.strategy.postType.PostFetchStrategy;
import com.backend.farmon.strategy.postType.PostFetchStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostQueryServiceImpl implements PostQueryService {

    private final PostFetchStrategyFactory strategyFactory;
    private final CommentRepository commentRepository;
    private final LikeCountRepository likeCountRepository;
    private final PostRepository postRepository;

    // 홈 화면 카테고리에 따른 커뮤니티 게시글 3개씩 조회
    // 인기, 전체, QNA, 전문가 칼럼
    @Override
    public HomeResponse.PostListDTO findHomePostsByCategory(PostType category) {

        // 카테고리별 게시글 조회
        PostFetchStrategy strategy = strategyFactory.getStrategy(category);
        List<Post> postList = strategy.fetchPosts(category);
        log.info("홈 화면 카테고리별 게시글 조회 성공");

        // 각 게시물의 좋아요 개수 조회
        List<Integer> likeCountList = postList.stream()
                .map(post -> likeCountRepository.countLikeCountsByPostId(post.getId()))
                .toList();
        log.info("홈 화면 카테고리별 게시글 좋아요 개수 조회 성공");

        // 각 게시물의 댓글 개수 조회
        List<Integer> commentCountList = postList.stream()
                .map(post -> commentRepository.countCommentsByPostId(post.getId()))
                .toList();
        log.info("홈 화면 카테고리별 게시글 댓글 개수 조회 성공");

        return HomeConverter.toPostListDTO(postList, likeCountList, commentCountList);
    }

    // 인기 전문가 칼럼 6개 조회
    @Override
    public HomeResponse.PopularPostListDTO findPopularExpertColumnPosts() {
        // 별도로 인기 칼럼으로 지정할 지정할 전문가 칼럼 게시글 아이디 리스트
        List<Long> popularPostsIdList = new ArrayList<>();
        popularPostsIdList.add(4L);

        // 인기 전문가 칼럼 6개 조회
        List<Post> expertColumnPostList = postRepository.findTop6ExpertColumnPostsByPostId(popularPostsIdList);
        log.info("홈 화면 인기 전문가 칼럼 조회 성공");

        return HomeConverter.toPopularPostListDTO(expertColumnPostList);
    }
}
