package com.backend.farmon.service.BoardService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.aws.s3.AmazonS3Manager;
import com.backend.farmon.converter.AnswerConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.domain.commons.TimeDifferenceUtil;
import com.backend.farmon.dto.Answer.AnswerRequestDTO;
import com.backend.farmon.dto.Answer.AnswerResponseDTO;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.post.PostResponseDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.AnswerRepository.AnswerImgRepository;
import com.backend.farmon.repository.AnswerRepository.AnswerRepository;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.PostRepository.PostImgRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository; // 게시글 데이터를 저장하기 위한 Repository
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service; // 파일 업로드를 위한 S3 서비스
    private final CropRepository cropRepository;
    private final AmazonS3Manager amazonS3Manager;
    private  final PostImgRepository postImgRepository;
    private final AnswerConverter answerConverter;
    private final AnswerImgRepository answerImgRepository;
    private final AnswerRepository answerRepository;

    @Override
    public PostResponseDTO save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles) throws Exception {

        log.info("저장 시작");
        log.info("postDto: " + postDto.getUserId());

        // 사용자 정보 확인
        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 게시판 정보 확인
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.FREE) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        // 게시글 생성
        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        List<String> imgUrls = new ArrayList<>();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            if (multipartFiles.size() > 5) {
                throw new IllegalArgumentException("사진은 최대 5개 까지만 업로드할 수 있습니다.");
            }
            for (MultipartFile imageFile : multipartFiles) {
                // 파일을 S3에 업로드
                String imageKey = "PostImg/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);

                // PostImg 객체 생성
                PostImg postImg = PostImg.builder()
                        .storedFileName(imageUrl)
                        .originalFileName(imageFile.getOriginalFilename())
                        .post(post)  // 현재 포스트와 연관
                        .build();

                // 생성된 PostImg 객체를 저장
                postImgRepository.save(postImg);

                // 이미지 URL 리스트에 추가
                imgUrls.add(imageUrl);
            }
        }

        // 게시글 작성 시간을 기준으로 시간 차 계산
        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());

        return new PostResponseDTO(post,imgUrls, timeAgo);
    }


    // 분야 선택 안 할 시 에러가 일어나게 에러 전문가 칼럼 과 qna 게시판에 추가
    @Override
    public PostResponseDTO save_QnaPost( BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles) throws Exception {
        validateFieldCategory(postDto.getCrop());
        log.info("검증은 완료");

        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        log.info("에러");
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.QNA) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        List<String> imgUrls = new ArrayList<>();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            if (multipartFiles.size() > 5) {
                throw new IllegalArgumentException("사진은 최대 5개 까지만 업로드할 수 있습니다.");
            }
            for (MultipartFile imageFile : multipartFiles) {
                // 파일을 S3에 업로드
                String imageKey = "PostImg/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);

                // PostImg 객체 생성
                PostImg postImg = PostImg.builder()
                        .storedFileName(imageUrl)
                        .originalFileName(imageFile.getOriginalFilename())
                        .post(post)  // 현재 포스트와 연관
                        .build();

                // 생성된 PostImg 객체를 저장
                postImgRepository.save(postImg);

                // 이미지 URL 리스트에 추가
                imgUrls.add(imageUrl);
            }
        }

        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());
        // null 넣어도 상관없지 않음?
        return new PostResponseDTO(post, imgUrls, timeAgo);
    }

    @Override
    public PostResponseDTO save_ExperCol(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles) throws Exception {
        validateFieldCategory(postDto.getCrop());
        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.EXPERT_COLUMN) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        List<String> imgUrls = new ArrayList<>();
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            if (multipartFiles.size() > 5) {
                throw new IllegalArgumentException("사진은 최대 5개 까지만 업로드할 수 있습니다.");
            }
            for (MultipartFile imageFile : multipartFiles) {
                // 파일을 S3에 업로드
                String imageKey = "PostImg/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);

                // PostImg 객체 생성
                PostImg postImg = PostImg.builder()
                        .storedFileName(imageUrl)
                        .originalFileName(imageFile.getOriginalFilename())
                        .post(post)  // 현재 포스트와 연관
                        .build();

                // 생성된 PostImg 객체를 저장
                postImgRepository.save(postImg);

                // 이미지 URL 리스트에 추가
                imgUrls.add(imageUrl);
            }
        }

        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());

        return new PostResponseDTO(post, imgUrls, timeAgo);
    }


    @Override
    public AnswerResponseDTO saveQnAAnswer(AnswerRequestDTO dto, List<MultipartFile> multipartFiles) throws Exception {
        // 글 쓴 사람 로직 파악
        log.info("User의 Id는 "+String.valueOf(dto.getUserId()));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        if(dto.getBoardId()!=1){
            throw new GeneralException(ErrorStatus.BOARD_TYPE_NOT_FOUND);
        }

        // 답변을 달 글이 실제로 존재하는가
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        // DTO -> 엔티티 변환 (답변)
        Answer answer = answerConverter.toEntity(dto);
        answerRepository.save(answer);
        // 이미지 업로드 처리 및 변환 (최대 5개 제한)
        List<String> imgUrls = new ArrayList<>();

            if (multipartFiles != null && !multipartFiles.isEmpty()) {
                if (multipartFiles.size() > 2) {
                    throw new IllegalArgumentException("사진은 최대 2개 까지만 업로드할 수 있습니다.");
                }
                for (MultipartFile imageFile : multipartFiles) {
                    // 파일을 S3에 업로드
                    String imageKey = "AnswerImg/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                    String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);
                    // PostImg 객체 생성
                    AnswerImg answerImg = AnswerImg.builder()
                            .storedFileName(imageUrl)
                            .originalFileName(imageFile.getOriginalFilename())
                            .answer(answer)
                            .build();

                    answerImgRepository.save(answerImg);
                    imgUrls.add(imageUrl);

                }

            }

        return new AnswerResponseDTO(answer, imgUrls);
    }


    private void validateFieldCategory(String crops) {
        log.info("검증 시작");
        log.info(crops);
        if (crops == null ) {
            throw new GeneralException(ErrorStatus.CROP_NOT_FOUND); // 존재하지 않는 작물 에러 발생
        }

        List<String> cropList = Arrays.stream(crops.split(",")) // 쉼표로 분리
                .map(String::trim) // 각 항목 공백 제거
                .filter(crop -> !crop.isEmpty()) // 빈 항목 제거
                .collect(Collectors.toList());

        log.info("에러1");
        if (cropList.isEmpty()) {
            throw new GeneralException(ErrorStatus.CROP_NOT_FOUND); // 잘못된 이름 에러 발생
        }

    }



    // 자유게시판 분야 지정X
    private Post createPostByBoardType(BoardRequestDto.FreePost postDTO,User user,Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .build();
    }


    // Qna,Expert 분야 지정 (상위 분야,하위분야 지정으로 저장)
    private Post createPostByBoardType(BoardRequestDto.QnaPost postDTO, User user, Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .Category(postDTO.getCategoryTitle()) // ✅ 상위 카테고리 저장
                .subCategories(postDTO.getCrop()) // ✅ 하위 카테고리 리스트 저장
                .user(user)
                .board(board)
                .build();
    }
    private Post createPostByBoardType(BoardRequestDto.ExpertColumn postDTO, User user, Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .Category(postDTO.getCategoryTitle()) // ✅ 상위 카테고리 저장
                .subCategories(postDTO.getCrop()) // ✅ 하위 카테고리 리스트 저장
                .user(user)
                .board(board)
                .build();
    }


    private void savePostToAllAndPopular(Post originalPost, User user) {
        // 전체 게시판 가져오기
        Board allBoard = boardRepository.findByPostType(PostType.ALL)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // 인기 게시판 가져오기
        Board popularBoard = boardRepository.findByPostType(PostType.POPULAR)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // 전체 게시판에 저장할 게시글 생성
        Post allPost = createPostByBoardAllPOPLUARType(originalPost, user, allBoard);
        postRepository.save(allPost);

        // 인기 게시판에 저장할 게시글 생성 (초기 좋아요 0 설정)
        Post popularPost = createPostByBoardAllPOPLUARType(originalPost, user, popularBoard);
        popularPost.setPostLikes(0); // 초기 좋아요 0으로 설정
        postRepository.save(popularPost);

        log.info("두 게시판에 모두 저장됨");
    }

    private Post createPostByBoardAllPOPLUARType(Post originalPost, User user, Board board) {
        return  Post.builder()
                .postTitle(originalPost.getPostTitle())
                .postContent(originalPost.getPostContent())
                .Category(originalPost.getCategory())
                .subCategories(originalPost.getSubCategories())
                .user(user)
                .board(board)
                .build();
    }


}