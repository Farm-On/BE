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
import com.backend.farmon.repository.BoardRepository.BoardPostRepository;
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
public class
BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository; // 게시글 데이터를 저장하기 위한 Repository
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service; // 파일 업로드를 위한 S3 서비스
    private final CropRepository cropRepository;
    private final AmazonS3Manager amazonS3Manager;
    private final PostImgRepository postImgRepository;
    private final AnswerConverter answerConverter;
    private final AnswerImgRepository answerImgRepository;
    private final AnswerRepository answerRepository;
    private final BoardPostRepository boardPostRepository;

    @Override
    public PostResponseDTO save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles) throws Exception {

        // 사용자 정보 확인
        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        // Free 게시판 확인
        Board freeBoard = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));
        if (freeBoard.getPostType() != PostType.FREE) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }
        // 내용물 확인
        validationContent(postDto, multipartFiles);
        // 원본 게시글 ID 설정 (Free 게시글의 ID를 사용)
        // Free 게시판에 게시글 생성 및 저장
        Post freePost = createPostByBoardType(postDto, user, freeBoard);
        postRepository.save(freePost);
        saveToBoard(freeBoard, freePost);

        Long originalPostId = freePost.getId();
        freePost.setOriginalPostId(originalPostId);

        // ALL 및 POPULAR 게시판 가져오기
        Board allBoard = boardRepository.findByPostType(PostType.ALL)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        Board popularBoard = boardRepository.findByPostType(PostType.POPULAR)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // ALL 및 POPULAR에 동일한 글 동기화
        Post allPost =  createPostByBoardType(postDto, user, allBoard);
        allPost.setOriginalPostId(originalPostId);
        postRepository.save(allPost);


        Post popularPost = createPostByBoardType(postDto, user, popularBoard);
        popularPost.setOriginalPostId(originalPostId);
        postRepository.save(popularPost);

        saveToBoard(allBoard, allPost);
        saveToBoard(popularBoard, popularPost);

        // 이미지 처리 및 저장
        List<String> imgUrls = new ArrayList<>();

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            if (multipartFiles.size() > 5) {
                throw new IllegalArgumentException("사진은 최대 5개 까지만 업로드할 수 있습니다.");
            }

            for (MultipartFile imageFile : multipartFiles) {
                // 파일을 S3에 업로드
                String imageKey = "PostImg/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);

                SaveImgFile(imageFile, imageUrl, freePost, imgUrls, originalPostId);
                SaveImgFile(imageFile, imageUrl, allPost, null, originalPostId);
                SaveImgFile(imageFile, imageUrl, popularPost, null, originalPostId);
            }
        }

        // 작성 시간 차 계산
        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(freePost.getCreatedAt());

        return new PostResponseDTO(freePost, imgUrls, timeAgo);
    }


    // 분야 선택 안 할 시 에러가 일어나게 에러 전문가 칼럼 과 qna 게시판에 추가
    @Override
    public PostResponseDTO save_QnaPost(BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles) throws Exception {
        validateFieldCategory(postDto.getCrop());

        User user = userRepository.findById(postDto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board = boardRepository.findById(postDto.getBoardId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        if (board.getPostType() != PostType.QNA) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }
        validationContent(postDto, multipartFiles);

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);
        saveToBoard(board, post);

        Long originalPostId = post.getId();
        post.setOriginalPostId(originalPostId);

        // ALL 및 POPULAR 게시판 가져오기
        Board allBoard = boardRepository.findByPostType(PostType.ALL)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        Board popularBoard = boardRepository.findByPostType(PostType.POPULAR)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // ALL 및 POPULAR에 동일한 글 동기화
        Post allPost =  createPostByBoardType(postDto, user, allBoard);
        allPost.setOriginalPostId(originalPostId);
        postRepository.save(allPost);


        Post popularPost = createPostByBoardType(postDto, user, popularBoard);
        popularPost.setOriginalPostId(originalPostId);
        postRepository.save(popularPost);

        saveToBoard(allBoard, allPost);
        saveToBoard(popularBoard, popularPost);

        List<String> imgUrls = new ArrayList<>();

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            if (multipartFiles.size() > 5) {
                throw new IllegalArgumentException("사진은 최대 5개 까지만 업로드할 수 있습니다.");
            }
            for (MultipartFile imageFile : multipartFiles) {
                // 파일을 S3에 업로드
                String imageKey = "PostImg/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);

                SaveImgFile(imageFile, imageUrl, post, imgUrls, originalPostId);
                SaveImgFile(imageFile, imageUrl, allPost, null, originalPostId);
                SaveImgFile(imageFile, imageUrl, popularPost, null, originalPostId);
                // PostImg 객체 생성
              //  SaveImgFile(imageFile, imageUrl, post, user, imgUrls);
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


        validationExpert(postDto, multipartFiles, board);

        Post Expertpost = createPostByBoardType(postDto, user, board);
        postRepository.save(Expertpost);
        saveToBoard(board,Expertpost);

        Long originalPostId = Expertpost.getId();
        Expertpost.setOriginalPostId(originalPostId);

        // ALL 및 POPULAR 게시판 가져오기
        Board allBoard = boardRepository.findByPostType(PostType.ALL)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        Board popularBoard = boardRepository.findByPostType(PostType.POPULAR)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_TYPE_NOT_FOUND));

        // ALL 및 POPULAR에 동일한 글 동기화
        Post allPost =  createPostByBoardType(postDto, user, allBoard);
        allPost.setOriginalPostId(originalPostId);
        postRepository.save(allPost);


        Post popularPost = createPostByBoardType(postDto, user, popularBoard);
        popularPost.setOriginalPostId(originalPostId);
        postRepository.save(popularPost);

        saveToBoard(allBoard, allPost);
        saveToBoard(popularBoard, popularPost);

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
                SaveImgFile(imageFile, imageUrl, Expertpost, imgUrls, originalPostId);
                SaveImgFile(imageFile, imageUrl, allPost, null, originalPostId);
                SaveImgFile(imageFile, imageUrl, popularPost, null, originalPostId);
            }
        }

        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(Expertpost.getCreatedAt());

        return new PostResponseDTO(Expertpost, imgUrls, timeAgo);
    }


    @Override
    public AnswerResponseDTO saveQnAAnswer(AnswerRequestDTO dto, List<MultipartFile> multipartFiles) throws Exception {
        // 글쓴 사람 확인
        log.info("User의 Id는 " + dto.getUserId());
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 답변을 달 글이 실제로 존재하는가
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

   

        if(dto.getBoardId()!=1){
            throw new GeneralException(ErrorStatus.BOARD_TYPE_NOT_FOUND);
        }

        // 원본 게시물 찾기 (원본 게시글은 originalPostId가 자기자신의 id여야 함)
        Post originalPost = postRepository.findById(post.getOriginalPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // originalPostId가 같은 모든 게시물 찾기
        List<Post> relatedPosts = postRepository.findAllByOriginalPostId(originalPost.getId());
        // 원본 게시글이 중복으로 저장되지 않도록 제외
        relatedPosts = relatedPosts.stream()
                .filter(p -> !p.getId().equals(originalPost.getId()))
                .collect(Collectors.toList());

        // DTO -> 엔티티 변환 (답변)
        Answer answer = answerConverter.toEntity(dto);
        answer.setPost(originalPost); // 원본 게시물 기준으로 저장
        answerRepository.save(answer);  // 우선 원본 답변 저장

        // 이미지 업로드 처리 및 변환 (최대 2개 제한)
        List<String> imgUrls = new ArrayList<>();
        List<AnswerImg> answerImgList = new ArrayList<>();

        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            if (multipartFiles.size() > 2) {
                throw new IllegalArgumentException("사진은 최대 2개까지만 업로드할 수 있습니다.");
            }

            for (MultipartFile imageFile : multipartFiles) {
                // 파일을 S3에 업로드
                String imageKey = "AnswerImg/" + UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
                String imageUrl = amazonS3Manager.uploadFile(imageKey, imageFile);

                // AnswerImg 객체 생성 후 리스트에 추가
                AnswerImg answerImg = AnswerImg.builder()
                        .storedFileName(imageUrl)
                        .originalFileName(imageFile.getOriginalFilename())
                        .answer(answer)  // 이미 저장된 원본 Answer와 연결
                        .build();
                answerImgList.add(answerImg);
                imgUrls.add(imageUrl);
            }
        }

        // 원본 답변에 대한 이미지 저장
        answerImgRepository.saveAll(answerImgList);

        // 관련 게시물에 대해 복사된 답변과 이미지를 생성 (원본 게시글은 제외)
        List<Answer> relatedAnswers = new ArrayList<>();
        List<AnswerImg> relatedImages = new ArrayList<>();

        for (Post relatedPost : relatedPosts) {
            Answer relatedAnswer = answerConverter.toEntity(dto);
            relatedAnswer.setPost(relatedPost);
            relatedAnswers.add(relatedAnswer);
        }

        // 관련 게시물에 대한 답변 한 번에 저장
        answerRepository.saveAll(relatedAnswers);

        // 각 관련 답변에 대해 원본 이미지 정보를 복사해서 저장
        for (Answer relatedAnswer : relatedAnswers) {
            for (AnswerImg originalImg : answerImgList) {
                AnswerImg relatedImg = AnswerImg.builder()
                        .storedFileName(originalImg.getStoredFileName())
                        .originalFileName(originalImg.getOriginalFileName())
                        .answer(relatedAnswer)
                        .build();
                relatedImages.add(relatedImg);
            }
        }

        // 관련 이미지 한 번에 저장
        answerImgRepository.saveAll(relatedImages);

        return new AnswerResponseDTO(answer, imgUrls);
    }




    private void SaveImgFile(MultipartFile imageFile, String imageUrl, Post post, List<String> imgUrls, Long originalPostId) {
        PostImg postImg = PostImg.builder()
                .storedFileName(imageUrl)
                .originalFileName(imageFile.getOriginalFilename())
                .post(post)
                .originalPostId(originalPostId)
                .build();

        postImgRepository.save(postImg);

        if (imgUrls != null) {
            imgUrls.add(imageUrl);
        }
    }



    private void validateFieldCategory(String crops) {
        log.info("검증 시작");
        log.info(crops);
        if (crops == null) {
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

    private static void validationContent(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles) {
        if (postDto.getPostTitle() == null && postDto.getSubTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_BOTH_NOT_FOUND);
        }
        if (postDto.getPostTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_TITILE_NOT_FOUND);
        }
        if (postDto.getSubTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_SUBTITLE_NOT_FOUND);
        }
        if (multipartFiles == null) {
            throw new GeneralException(ErrorStatus.POST_PICTURES_NOT_FOUND);
        }
        if (postDto.getPostContent() == null) {
            throw new GeneralException(ErrorStatus.POST_CONTENT_NOT_FOUND);
        }
    }

    private static void validationContent(BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles) {
        if (postDto.getPostTitle() == null && postDto.getSubTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_BOTH_NOT_FOUND);
        }
        if (postDto.getPostTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_TITILE_NOT_FOUND);
        }
        if (postDto.getSubTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_SUBTITLE_NOT_FOUND);
        }
        if(postDto.getCategoryTitle()==null) {
            throw new GeneralException(ErrorStatus.FIELD_CATEGORY_REQUIRED);
        }
        if (multipartFiles == null) {
            throw new GeneralException(ErrorStatus.POST_PICTURES_NOT_FOUND);
        }
        if (postDto.getPostContent() == null) {
            throw new GeneralException(ErrorStatus.POST_CONTENT_NOT_FOUND);
        }
    }



    // 새로운 Post 객체를 생성하여 다른 게시판과 연관
    private Post clonePostForBoard(Post original) {
        Post clonedPost = new Post();
        clonedPost.setPostTitle(original.getPostTitle());
        clonedPost.setPostContent(original.getPostContent());
        clonedPost.setPostLikes(0); // 좋아요 초기화 (필요 시)
        return clonedPost;
    }



    private void saveToBoard(Board board, Post post) {
        if (post.getBoardPosts() == null) {
            post.setBoardPosts(new ArrayList<>());
        }
        BoardPost boardPost = new BoardPost();
        boardPost.setBoard(board);
        boardPost.setPost(post);
        board.getBoardPosts().add(boardPost);
        post.getBoardPosts().add(boardPost);
        boardPostRepository.save(boardPost);
    }

    private static void validationExpert(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles, Board board) {
        if (board.getPostType() != PostType.EXPERT_COLUMN) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }
        if (postDto.getPostTitle() == null && postDto.getSubTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_BOTH_NOT_FOUND);
        }
        if (postDto.getPostTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_TITILE_NOT_FOUND);
        }
        if (postDto.getSubTitle() == null) {
            throw new GeneralException(ErrorStatus.POST_SUBTITLE_NOT_FOUND);
        }
        if (multipartFiles == null) {
            throw new GeneralException(ErrorStatus.POST_PICTURES_NOT_FOUND);
        }
        if (postDto.getCategoryTitle() == null) {
            throw new GeneralException(ErrorStatus.FIELD_CATEGORY_REQUIRED);
        }

        if (postDto.getPostContent() == null) {
            throw new GeneralException(ErrorStatus.POST_CONTENT_NOT_FOUND);
        }
    }

    // 자유게시판 분야 지정 X
    private Post createPostByBoardType(BoardRequestDto.FreePost postDTO, User user, Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .build();
    }

    // 전체게시판 분야 지정 O
    private Post createPostAllByBoardType(BoardRequestDto.AllPost postDTO, User user, Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .Category(postDTO.getCategoryTitle()) // ✅ 상위 카테고리 저장
                .subCategories(postDTO.getCrop()) // ✅ 하위 카테고리 리스트 저장
                .user(user)
                .board(board)
                .build();
    }

    // 인기 게시판 분야 지정 O
    private Post createPostPopularByBoardType(BoardRequestDto.PopularPost postDTO, User user, Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .Category(postDTO.getCategoryTitle()) // ✅ 상위 카테고리 저장
                .subCategories(postDTO.getCrop()) // ✅ 하위 카테고리 리스트 저장
                .user(user)
                .board(board)
                .build();
    }


    // Qna,Expert 분야 지정 (상위 분야,하위분야 지정으로 저장)
    private Post createPostByBoardType(BoardRequestDto.QnaPost postDTO, User user, Board board) {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
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
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .Category(postDTO.getCategoryTitle()) // ✅ 상위 카테고리 저장
                .subCategories(postDTO.getCrop()) // ✅ 하위 카테고리 리스트 저장
                .user(user)
                .board(board)
                .build();
    }
}
