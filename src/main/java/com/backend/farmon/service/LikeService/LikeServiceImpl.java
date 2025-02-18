package com.backend.farmon.service.LikeService;


import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.domain.LikeCount;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.repository.LikeCountRepository.LikeCountRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeCountRepository likeCountRepository;
    private final UserAuthorizationUtil userAuthorizationUtil;


    // 좋아요 추가
    @Transactional
    public void postLikeUp(Long userId, Long postId) throws IllegalAccessException {
        String currentUserRole = userAuthorizationUtil.getCurrentUserRole();

        if (!"FARMER".equals(currentUserRole) && !"EXPERT".equals(currentUserRole)) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_ACCESS);
        }
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        // 원본 게시물 찾기 (originalPostId가 null인 게시물)

        Post originalPost = post.getOriginalPostId() == null ? post
                : postRepository.findById(post.getOriginalPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // originalPostId가 같은 모든 게시물 가져오기
        List<Post> relatedPosts = postRepository.findAllByOriginalPostId(originalPost.getId());

        if (user.equals(post.getUser())) {
            throw new GeneralException(ErrorStatus.LIKE_TYPE_NOT_SAVED);
        }

        // 중복 좋아요 방지 (originalPostId가 같은 모든 게시물 체크)
        for (Post relatedPost : relatedPosts) {
            if (likeCountRepository.findByUserIdAndPostId(userId, relatedPost.getId()) != null) {
                throw new IllegalAccessException("이미 좋아요를 눌렀습니다!");
            }
        }

        // 원본 게시물 기준으로 좋아요 저장
        LikeCount likeCount = LikeCount.builder()
                .user(user)
                .post(originalPost) // 원본 게시물로 저장
                .build();
        likeCountRepository.save(likeCount);

        // originalPostId가 같은 모든 게시물의 좋아요 증가
        for (Post relatedPost : relatedPosts) {
            relatedPost.increaseLikes();
        }

        postRepository.saveAll(relatedPosts);
        postRepository.flush();
    }

    // 좋아요 감소
    @Transactional
    public void postLikeDown(Long userId, Long postId) throws IllegalAccessException {

        String currentUserRole = userAuthorizationUtil.getCurrentUserRole();

        if (!"FARMER".equals(currentUserRole) && !"EXPERT".equals(currentUserRole)) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_ACCESS);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));


        // 원본 게시물 찾기
        Post originalPost = post.getOriginalPostId() == null ? post
                : postRepository.findById(post.getOriginalPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 좋아요 찾기 (원본 게시물 기준)
        LikeCount like = likeCountRepository.findByUserIdAndPostId(userId, originalPost.getId());
        if (like == null) {
            throw new IllegalAccessException("좋아요를 누른 적이 없습니다!");
        }

        // 좋아요 삭제
        likeCountRepository.delete(like);

        // originalPostId가 같은 모든 게시물 가져오기
        List<Post> relatedPosts = postRepository.findAllByOriginalPostId(originalPost.getId());

        // originalPostId가 같은 모든 게시물의 좋아요 감소
        for (Post relatedPost : relatedPosts) {
            relatedPost.decreaseLikes();
        }

        postRepository.saveAll(relatedPosts);
        postRepository.flush();

    }


    // 좋아요 개수 조회
    @Transactional
    public int getLikeCount(Long postId) {
        return postRepository.getLikeCount(postId);
    }



}
