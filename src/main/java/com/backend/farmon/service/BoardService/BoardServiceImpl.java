package com.backend.farmon.service.BoardService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.domain.Board;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.PostImg;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.commons.TimeDifferenceUtil;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.post.PostResponseDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

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


    @Override
    public PostResponseDTO save_FreePost(BoardRequestDto.FreePost postDto) throws Exception {

        log.info("저장 시작");
        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.FREE) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        List<String> imgUrls = new ArrayList<>();
        if (postDto.getImgList() != null && !postDto.getImgList().isEmpty()) {
            for (String base64Image : postDto.getImgList()) {
                try {
                    // Base64 데이터 부분 추출 (이미지 앞부분의 'data:image/png;base64,' 등의 부분을 제거)
                    if (base64Image.contains(",")) {
                        base64Image = base64Image.split(",")[1]; // Base64 데이터 부분만 추출
                    }

                    // Base64 문자열에 대해 공백 제거 및 유효성 검사
                    base64Image = base64Image.trim();

                    // Base64 디코딩 (에러 처리 추가)
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

                    // 이미지 업로드 로직 (S3 업로드 등)
                    PostImg postImg = s3Service.saveImage(decodedBytes, post); // S3 업로드 로직 호출
                    String imgUrl = s3Service.getFullPath(postImg.getStoredFileName()); // 저장된 파일명에서 URL 생성
                    imgUrls.add(imgUrl);
                } catch (IllegalArgumentException e) {
                    // Base64 디코딩 오류가 발생하면 로그를 남기고 해당 이미지를 무시하거나 예외 처리
                    log.info("Base64 decoding error for image: " + base64Image);
                    // 필요시 로그를 남기거나 해당 이미지를 무시
                }
            }
        }
        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());
        return new PostResponseDTO(post, imgUrls, timeAgo);
    }

    // 분야 선택 안 할 시 에러가 일어나게 에러 전문가 칼럼 과 qna 게시판에 추가
    @Override
    public PostResponseDTO save_QnaPost(BoardRequestDto.QnaPost postDto) throws Exception {
        validateFieldCategory(postDto.getCrop());

        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.QNA) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);

        List<String> imgUrls = new ArrayList<>();
        if (postDto.getImgList() != null && !postDto.getImgList().isEmpty()) {
            for (String base64Image : postDto.getImgList()) {
                try {
                    // Base64 데이터 부분 추출 (이미지 앞부분의 'data:image/png;base64,' 등의 부분을 제거)
                    if (base64Image.contains(",")) {
                        base64Image = base64Image.split(",")[1]; // Base64 데이터 부분만 추출
                    }

                    // Base64 문자열에 대해 공백 제거 및 유효성 검사
                    base64Image = base64Image.trim();

                    // Base64 디코딩 (에러 처리 추가)
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

                    // 이미지 업로드 로직 (S3 업로드 등)
                    PostImg postImg = s3Service.saveImage(decodedBytes, post); // S3 업로드 로직 호출
                    String imgUrl = s3Service.getFullPath(postImg.getStoredFileName()); // 저장된 파일명에서 URL 생성
                    imgUrls.add(imgUrl);
                } catch (IllegalArgumentException e) {
                    // Base64 디코딩 오류가 발생하면 로그를 남기고 해당 이미지를 무시하거나 예외 처리
                    log.info("Base64 decoding error for image: " + base64Image);
                    // 필요시 로그를 남기거나 해당 이미지를 무시
                }
            }
        }
        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());
        return new PostResponseDTO(post, imgUrls, timeAgo);
    }



    @Override
    public PostResponseDTO save_ExperCol(BoardRequestDto.ExpertColumn postDto) throws Exception {
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
        if (postDto.getImgList() != null && !postDto.getImgList().isEmpty()) {
            for (String base64Image : postDto.getImgList()) {
                try {
                    // Base64 데이터 부분 추출 (이미지 앞부분의 'data:image/png;base64,' 등의 부분을 제거)
                    if (base64Image.contains(",")) {
                        base64Image = base64Image.split(",")[1]; // Base64 데이터 부분만 추출
                    }

                    // Base64 문자열에 대해 공백 제거 및 유효성 검사
                    base64Image = base64Image.trim();

                    // Base64 디코딩 (에러 처리 추가)
                    byte[] decodedBytes = Base64.getDecoder().decode(base64Image);

                    // 이미지 업로드 로직 (S3 업로드 등)
                    PostImg postImg = s3Service.saveImage(decodedBytes, post); // S3 업로드 로직 호출
                    String imgUrl = s3Service.getFullPath(postImg.getStoredFileName()); // 저장된 파일명에서 URL 생성
                    imgUrls.add(imgUrl);
                } catch (IllegalArgumentException e) {
                    // Base64 디코딩 오류가 발생하면 로그를 남기고 해당 이미지를 무시하거나 예외 처리
                    log.info("Base64 decoding error for image: " + base64Image);
                    // 필요시 로그를 남기거나 해당 이미지를 무시
                }
            }
        }
        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());
        return new PostResponseDTO(post, imgUrls, timeAgo);
    }





    private void validateFieldCategory(String crops) {
        log.info("검증 시작");

        if (crops == null) {
            throw new GeneralException(ErrorStatus.CROP_NOT_FOUND); // 존재하지 않는 작물 에러 발생
        }

        // crops가 단일 문자열인지 확인 (쉼표 포함 여부)
        if (!crops.contains(",")) {
            log.info("단일 작물 검증: {}", crops);
            if (!cropRepository.findByName(crops.trim()).isPresent()) {
                throw new GeneralException(ErrorStatus.CROP_NOT_FOUND);
            }
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
                .Category(postDTO.getCategoryTitle())
                .subCategories(postDTO.getCrop())
                .user(user)
                .board(board)
                .build();
    }
    private Post createPostByBoardType(BoardRequestDto.ExpertColumn postDTO, User user, Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .Category(postDTO.getCategoryTitle())
                .subCategories(postDTO.getCrop())
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