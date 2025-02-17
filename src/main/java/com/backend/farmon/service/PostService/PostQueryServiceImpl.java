package com.backend.farmon.service.PostService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.converter.HomeConverter;
import com.backend.farmon.converter.PostConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.domain.commons.TimeDifferenceUtil;
import com.backend.farmon.dto.Answer.AnswerResponseDTO;
import com.backend.farmon.dto.Comment.CommentResponseDTO;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostPagingResponseDTO;
import com.backend.farmon.dto.post.PostResponseDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.dto.post.PostWithAnswersResponseDTO;
import com.backend.farmon.repository.AnswerRepository.AnswerRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.backend.farmon.dto.post.PostType.QNA;


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
    private final AnswerRepository answerRepository;
    private static final Integer POST_LIMIT=3;

    // 홈 화면 카테고리에 따른 커뮤니티 게시글 3개씩 조회
    // 인기, 전체, QNA, 전문가 칼럼
    @Override
    public HomeResponse.PostListDTO findHomePostsByCategory(PostType category) {

        // 카테고리별 게시글 조회
        PostFetchStrategy strategy = strategyFactory.getStrategy(category);
        List<Post> postList = strategy.fetchPosts(category, POST_LIMIT);
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
    public Page<PostPagingResponseDTO> findAllPostsByBoardPK(Long boardId, int page, int size, String sortStr, List<String> crops) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> postPages = (crops == null || crops.isEmpty())
                ? postRepository.findAllByBoardId(boardId, pageable)
                :postRepository.findPostsByBoardIdAndCrops(boardId, crops, pageable);

        // Post 객체를 PostPagingResponseDTO로 변환하고 S3 URL을 포함하여 반환
        return postPages.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));
    }

    // 인기 게시판 좋아요 순
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findPopularPosts(Long boardId, int pageNum, int size, String sort, List<String> crops) {
        Sort.Direction direction = Sort.Direction.fromString(sort);
        Pageable pageable = PageRequest.of(pageNum - 1, size, Sort.by(direction, "postLikes"));

        Page<Post> posts = (crops == null || crops.isEmpty())
                ? postRepository.findPopularPosts(boardId, pageable)
                : postRepository.findPostsByBoardIdAndCrops(boardId, crops, pageable);

        return posts.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));
    }

    // Qna 글 조회
    @Transactional(readOnly = true)
    public Page<PostPagingResponseDTO> findQnaPostsByBoardPK(Long boardId, int page, int size, String sortStr, List<String> crops) {
        // 정렬 방향 설정: 'ASC' 또는 'DESC' 기준으로 생성일(createdAt)로 정렬 기본이 DESC
        Sort sort = Sort.by(Sort.Direction.fromString(sortStr), "createdAt");
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        Page<Post> posts = (crops == null || crops.isEmpty())
                ? postRepository.findAllByBoardId(boardId, pageable)
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
                ? postRepository.findAllByBoardId(boardId, pageable)
                : postRepository.findPostsByBoardIdAndCrops(boardId, crops, pageable);

        return posts.map(post -> new PostPagingResponseDTO(post, s3Service.getFullPath(post.getPostImgs())));
    }


    @Transactional(readOnly = true)
    public PostResponseDTO getBoardIdAndPostById(Long boardId, Long postId) {
        // 1. 게시판 존재 여부 확인
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_TYPE_NOT_FOUND));

        // 2. 게시글 존재 여부 확인 (지정된 postId로 조회)
        Post post = postRepository.findByIdWithComments(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 3. 댓글 조회를 위해, originalPostId가 같다면 해당 originalPostId를 기준으로 모든 게시글을 동기화한다.
        List<Post> postsForComments;
        if (post.getOriginalPostId() != null) {
            postsForComments = postRepository.findByOriginalPostIdWithComments(post.getOriginalPostId());
        } else {
            postsForComments = Collections.singletonList(post); // 원본 게시글만 포함
        }

        if (postsForComments.isEmpty()) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        // 4. 댓글 동기화를 위해 첫 번째 게시글을 기준으로 사용
        Post postForComments = postsForComments.get(0);

        // 5. 이미지 URL 생성 (S3의 전체 URL을 생성)
        List<PostImg> imgs = post.getPostImgs();
        List<String> imgUrls = imgs.stream()
                .map(img -> s3Service.getFullPath(img.getStoredFileName()))
                .collect(Collectors.toList());

        // 6. 작성 시간 차이 계산
        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());

        // 7. 댓글 데이터 조회 및 변환
        // 최상위 댓글(부모가 null인 댓글)만 필터링
        List<CommentResponseDTO> comments = postForComments.getComments().stream()
                .filter(comment -> comment.getParent() == null) // 최상위 댓글만 필터링
                .map(parentComment -> {
                    // 대댓글 중복 제거 (originalCommentId 기준으로 고유한 대댓글만 포함)
                    List<CommentResponseDTO> uniqueChildren = parentComment.getChildren().stream()
                            .collect(Collectors.toMap(
                                    Comment::getOriginalCommentId, // key: originalCommentId
                                    CommentResponseDTO::new,       // value: CommentResponseDTO 객체
                                    (existing, replacement) -> existing // 중복 발생 시 기존 값 유지
                            ))
                            .values()
                            .stream()
                            .collect(Collectors.toList());

                    // 부모 댓글에 고유한 대댓글 리스트를 설정
                    CommentResponseDTO parentDto = new CommentResponseDTO(parentComment);
                    parentDto.setChildren(uniqueChildren);
                    return parentDto;
                })
                .collect(Collectors.toList());

        // 8. PostResponseDTO 반환 (게시글, 이미지 URL, 시간 차, 댓글 포함)
        return new PostResponseDTO(post, imgUrls, timeAgo, comments);
    }



    // Qna 게시판용 상세 조회
    @Transactional(readOnly = true)
    public PostWithAnswersResponseDTO getBoardIdAndQnAPostById(Long boardId, Long postId) {
        // 1. Board 조회
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.BOARD_TYPE_NOT_FOUND));

        // 2. Post 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 3. Board 타입 검증 (QNA 타입인지 확인)
        if (board.getPostType() != PostType.QNA) {
            throw new GeneralException(ErrorStatus.BOARD_TYPE_NOT_FOUND);
        }

        // 4. Post에 연결된 Answer 리스트 가져오기
        List<Answer> answers = post.getAnswers();

        // 5. Answer 리스트를 AnswerResponseDTO로 변환
        List<AnswerResponseDTO> answerResponseDTOs = answers.stream()
                .map(answer -> {
                    // 이미지 URL 리스트 생성
                    List<String> imgUrls = answer.getAnswerImgList().stream()
                            .map(img -> s3Service.getFullPath(img.getStoredFileName())) // S3 URL 생성
                            .collect(Collectors.toList());

                    // DTO 생성
                    return AnswerResponseDTO.builder()
                            .answer(answer)
                            .imgUrls(imgUrls)
                            .build();
                })
                .collect(Collectors.toList());

        // 6. Post 정보 DTO 변환
        PostResponseDTO postResponseDTO = PostResponseDTO.builder()
                .postId(post.getId())
                .postTitle(post.getPostTitle())
                .postContent(post.getPostContent())
                .Category(post.getCategory())
                .subCategory(post.getSubCategories())
                .createdAt(String.valueOf(post.getCreatedAt()))
                .build();

        // 7. 최종 DTO 반환
        return PostWithAnswersResponseDTO.builder()
                .post(postResponseDTO)
                .answers(answerResponseDTOs)
                .build();
    }



}
