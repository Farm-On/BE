package com.backend.farmon.service.PostService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.converter.HomeConverter;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.CommendRepository.CommentRepository;
import com.backend.farmon.repository.LikeCountRepository.LikeCountRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
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
    private final UserRepository userRepository;
    private final UserAuthorizationUtil userAuthorizationUtil;

    // 홈 화면 카테고리에 따른 커뮤니티 게시글 3개씩 조회
    // 인기, 전체, QNA, 전문가 칼럼
    @Override
    public HomeResponse.PostListDTO findHomePostsByCategory(Long userId, PostType category) {
        if(userId!=null){
            User user = userRepository.findById(userId)
                    .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

            if(!userAuthorizationUtil.getCurrentUserId().equals(userId)){
                log.error("userId 불일치, 로그인 userId: {}, 파라미터 userId: {}", userAuthorizationUtil.getCurrentUserId(), userId);
                // 에러 던지기
            }
        }

        // 카테고리별 게시글 조회
        PostFetchStrategy strategy = strategyFactory.getStrategy(category);
        List<Post> postList = strategy.fetchPosts();
        log.info("홈 화면 카테고리별 게시글 조회 성공, userId: {}", userId);

        // 각 게시물의 좋아요 개수 조회
        List<Integer> likeCountList = postList.stream()
                .map(post -> likeCountRepository.countLikeCountsByPostId(post.getId()))
                .toList();
        log.info("홈 화면 카테고리별 게시글 좋아요 개수 조회 성공, userId: {}", userId);

        // 각 게시물의 댓글 개수 조회
        List<Integer> commentCountList = postList.stream()
                .map(post -> commentRepository.countCommentsByPostId(post.getId()))
                .toList();
        log.info("홈 화면 카테고리별 게시글 댓글 개수 조회 성공, userId: {}", userId);

        return HomeConverter.toPostListDTO(postList, likeCountList, commentCountList);
    }
}
