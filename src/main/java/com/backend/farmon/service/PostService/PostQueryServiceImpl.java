package com.backend.farmon.service.PostService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.converter.HomeConverter;
import com.backend.farmon.converter.PostConverter;
import com.backend.farmon.domain.Board;
import com.backend.farmon.domain.Crop;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.PostImg;
import com.backend.farmon.domain.commons.TimeDifferenceUtil;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostPagingResponseDTO;
import com.backend.farmon.dto.post.PostResponseDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.backend.farmon.repository.CommentRepository.CommentRepository;
import com.backend.farmon.repository.LikeCountRepository.LikeCountRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.service.AWS.S3Service;
import com.backend.farmon.strategy.postType.PostFetchStrategy;
import com.backend.farmon.strategy.postType.PostFetchStrategyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostQueryServiceImpl implements PostQueryService {

    private final PostFetchStrategyFactory strategyFactory;
    private final CommentRepository commentRepository;
    private final LikeCountRepository likeCountRepository;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;
    private final S3Service s3Service;

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

    //전체 게시판 좋아요 순
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findAllPostsByBoardPK(Long boardId, int page, int size, String sortStr, List<Crop> crops) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> postPages = (crops == null || crops.isEmpty())
                ? postRepository.findAllByBoardId(boardId, pageable)
                : postRepository.findPostsByBoardIdAndCrops(boardId, crops.stream().map(Crop::getName).collect(Collectors.toList()), pageable);

        // Post 객체를 PostPagingResponseDTO로 변환하고 S3 URL을 포함하여 반환
        return postPages.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));
    }

    // 인기 게시판 좋아요 순
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findPopularPosts(Long boardId, int pageNum, int size, String sort, List<Crop> crops) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(pageNum - 1, size, Sort.by(direction, "postLikes"));

        Page<Post> posts = (crops == null || crops.isEmpty())
                ? postRepository.findPopularPosts(boardId, pageable)
                : postRepository.findPostsByBoardIdAndCrops(boardId, crops.stream().map(Crop::getName).collect(Collectors.toList()), pageable);

        return posts.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));
    }

    // Qna 글 조회
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findQnaPostsByBoardPK(Long boardId, int page, int size, String sortStr, List<String> crops) {
        // 정렬 방향 설정: 'ASC' 또는 'DESC' 기준으로 생성일(createdAt)로 정렬 기본이 DESC
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> posts = (crops == null || crops.isEmpty())
                ? postRepository.findPopularPosts(boardId, pageable)
                : postRepository.findPostsByBoardIdAndCrops(boardId, crops, pageable);

        return posts.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));
    }



    // 전문가 글 조회
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findExpertsPostsByBoardPK(Long boardId, int page, int size, String sortStr, List<String> crops) {
        // 정렬 방향 설정: 'ASC' 또는 'DESC' 기준으로 생성일(createdAt)로 정렬 기본이 DESC
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> posts = (crops == null || crops.isEmpty())
                ? postRepository.findPopularPosts(boardId, pageable)
                : postRepository.findPostsByBoardIdAndCrops(boardId, crops, pageable);

        return posts.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));
    }

    ////모든  글 상세 조회
    @Transactional(readOnly = true)
    public PostResponseDTO getBoardIdAndPostById(Long boardId,Long postId) {
        Board board=boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_TYPE_NOT_FOUND));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        List<PostImg>imgs=post.getPostImgs();
        List<String>imgUrls=new ArrayList<>();
        if(imgs !=null){
            for(PostImg img :imgs){
                imgUrls.add(s3Service.getFullPath(img.getStoredFileName()));
            }
        }

        // 작성 시간 차이 계산
        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());

        return new PostResponseDTO(post,imgUrls,timeAgo);
    }

}
