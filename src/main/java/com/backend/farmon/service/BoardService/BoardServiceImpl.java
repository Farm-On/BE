package com.backend.farmon.service.BoardService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.aws.s3.AmazonS3Manager;
import com.backend.farmon.config.security.UserAuthorizationUtil;
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
import com.backend.farmon.repository.CommentRepository.CommentRepository;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.LikeCountRepository.LikeCountRepository;
import com.backend.farmon.repository.PostRepository.PostImgRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
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
    private final CommentRepository commentRepository;
    private final PostImgRepository postImgRepository;
    private final AnswerConverter answerConverter;
    private final AnswerImgRepository answerImgRepository;
    private final AnswerRepository answerRepository;
    private final BoardPostRepository boardPostRepository;
    private final LikeCountRepository likeCountRepository;
    private final UserAuthorizationUtil userAuthorizationUtil;

    @Override
    public PostResponseDTO save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles) throws Exception {
        String currentUserRole = userAuthorizationUtil.getCurrentUserRole();

        if (!"FARMER".equals(currentUserRole) && !"EXPERT".equals(currentUserRole)) {
            throw new GeneralException(ErrorStatus.UNAUTHORIZED_ACCESS);
        }
        // 자유게시판에서 사용자 역할은 전문가와 농업인만 글을 쓸 수 있다.

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

        String currentUserRole = userAuthorizationUtil.getCurrentUserRole();

        if (!"FARMER".equals(currentUserRole)) {
            throw new GeneralException(ErrorStatus.FARMER_ONLY_ACCESS);
        }

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

            }
        }

        String timeAgo = TimeDifferenceUtil.calculateTimeDifference(post.getCreatedAt());
        // null 넣어도 상관없지 않음?
        return new PostResponseDTO(post, imgUrls, timeAgo);
    }

    @Override
    public PostResponseDTO save_ExperCol(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles) throws Exception {
        String currentUserRole = userAuthorizationUtil.getCurrentUserRole();

        if (!"EXPERT".equals(currentUserRole)) {
            throw new GeneralException(ErrorStatus.EXPERT_ONLY_ACCESS);
        }

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
        String currentUserRole = userAuthorizationUtil.getCurrentUserRole();

        if (!"EXPERT".equals(currentUserRole)) {
            throw new GeneralException(ErrorStatus.EXPERT_ONLY_ACCESS);
        }

        // 글쓴 사람 확인
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));

        // 답변을 달 글이 실제로 존재하는가
        Post post = postRepository.findById(dto.getPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if(dto.getBoardId() != 1){
            throw new GeneralException(ErrorStatus.BOARD_TYPE_NOT_FOUND);
        }

        // 원본 게시물 찾기 (원본 게시글은 originalPostId가 자기자신의 id여야 함)
        Post originalPost = postRepository.findById(post.getOriginalPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));
        List<Post> relatedPosts = postRepository.findAllByOriginalPostId(originalPost.getId());
        // 원본 게시글이 중복으로 저장되지 않도록 제외
        relatedPosts = relatedPosts.stream()
                .filter(p -> !p.getId().equals(originalPost.getId()))
                .collect(Collectors.toList());

        // 원본 게시물에 이미 답변이 존재하는지 확인 (중복 방지)
        Optional<Answer> existingAnswer = answerRepository.findByPostAndUser(originalPost, user);
        if (existingAnswer.isPresent()) {
            // 이미 답변이 존재하면 예외 처리 또는 기존 답변 반환
            throw new GeneralException(ErrorStatus.ANSWER_ALREADY_EXISTS);
        }

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
    @Transactional
    public AnswerResponseDTO deleteQnAAnswer(Long answerId, Long userId) {
        Long currentUserId = userAuthorizationUtil.getCurrentUserId(); // 로그인한 유저 ID 가져오기



        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.USER_NOT_FOUND));
        
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.ANSWER_NOT_FOUND));


        // 해당 답변의 작성자가 userId와 일치하는지 확인
        if (!answer.getUser().getId().equals(userId)) {
            throw new GeneralException(ErrorStatus.DELETE_ONLY_ACCESS);
        }

        // 해당 답변에 대한 이미지 리스트 찾기
        List<AnswerImg> answerImgs = answerImgRepository.findAllByAnswerId(answerId);

        // 해당 답변이 속한 게시글 (Post) 찾기
        Post post = answer.getPost();  // 이미 Answer 엔티티에 연관된 Post가 있으므로, 추가로 찾을 필요 없음
        Post originalPost = postRepository.findById(post.getOriginalPostId())
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        // 원본 게시물의 ID (originalPostId)를 이용해 관련된 모든 게시물 찾기
        List<Post> relatedPosts = postRepository.findAllByOriginalPostId(originalPost.getId());

        // S3에서 이미지 삭제
        for (AnswerImg img : answerImgs) {
            // S3에서 이미지 삭제: 파일 위치를 "AnswerImg/"로 수정
            String s3Key = "AnswerImg/" + img.getStoredFileName();
            amazonS3Manager.deleteFile(s3Key);
        }

        // DB에서 이미지 삭제
        answerImgRepository.deleteAllByAnswerId(answerId);

        // 해당 답변 삭제
        answerRepository.delete(answer);

        // 원본 게시글에 속한 모든 답변 삭제
        for (Post relatedPost : relatedPosts) {
            List<Answer> relatedAnswers = answerRepository.findAllByPostId(relatedPost.getId());

            for (Answer relatedAnswer : relatedAnswers) {
                // 답변 작성자 확인
                if (!relatedAnswer.getUser().getId().equals(userId)) {
                    continue; // 본인 답변이 아니면 삭제하지 않음
                }

                List<AnswerImg> relatedAnswerImgs = answerImgRepository.findAllByAnswerId(relatedAnswer.getId());

                // 관련된 답변 이미지 삭제
                for (AnswerImg img : relatedAnswerImgs) {
                    String s3Key = "AnswerImg/" + img.getStoredFileName();
                    amazonS3Manager.deleteFile(s3Key);
                }

                // DB에서 이미지 삭제
                answerImgRepository.deleteAllByAnswerId(relatedAnswer.getId());

                // 관련 답변 삭제
                answerRepository.delete(relatedAnswer);
            }
        }

        // 삭제된 답변 정보를 DTO로 변환하여 반환
        return new AnswerResponseDTO(answer, extractImgUrls(answerImgs));
    }




    private List<String> extractImgUrls(List<AnswerImg> answerImgs) {
        // 이미지 URL을 추출하여 리스트로 반환하는 메서드
        return answerImgs.stream()
                .map(AnswerImg::getStoredFileName)
                .collect(Collectors.toList());
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

        if (postDto.getPostContent() == null) {
            throw new GeneralException(ErrorStatus.POST_CONTENT_NOT_FOUND);
        }
    }


    // 삭제 매커니즘

    // 자유게시판
    @Override
    @Transactional
    public PostResponseDTO deleteFreePost(Long postId) {
        return deletePost(postId, PostType.FREE);
    }

    // QNA 게시판
    @Override
    @Transactional
    public PostResponseDTO deleteQnaPost(Long postId) {
        return deletePost(postId, PostType.QNA);
    }

    // 전문가칼럼 게시판
    @Override
    @Transactional
    public PostResponseDTO deleteExpertColumnPost(Long postId) {
        return deletePost(postId, PostType.EXPERT_COLUMN);
    }

    private String extractS3KeyFromUrl(String imageUrl) {
        return imageUrl.substring(imageUrl.indexOf("PostImg/"));
        // 예: https://s3.amazonaws.com/bucket-name/estimate/UUID_filename.jpg
        // -> estimate/UUID_filename.jpg (S3에서 삭제할 key)
    }
    private PostResponseDTO deletePost(Long postId, PostType postType) {
        // 1) 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Long currentUserId = userAuthorizationUtil.getCurrentUserId();
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new GeneralException(ErrorStatus.DELETE_ONLY_ACCESS);
        }


        // 2) 관련 댓글을 isDeleted = true로 설정
        List<Comment> comments = commentRepository.findAllByPostId(postId);
        for (Comment comment : comments) {
            comment.setIsDeleted(true);
        }
        commentRepository.saveAll(comments);

        // 3) 관련된 좋아요 정보 삭제
        List<LikeCount> likeCounts = likeCountRepository.findAllByPostId(postId); // 해당 게시글의 좋아요 정보 조회
        likeCountRepository.deleteAll(likeCounts); // 해당 게시글과 관련된 모든 좋아요 삭제

        // 4) S3에서 이미지 삭제
        List<PostImg> postImgs = postImgRepository.findByPostId(postId);
        for (PostImg postImg : postImgs) {
            String s3key = "PostImg/" + postImg.getStoredFileName();
            amazonS3Manager.deleteFile(s3key);
        }

        // 5) 게시글 삭제
        postRepository.delete(post);

        // 6) ALL 및 POPULAR 게시판에서도 해당 게시글 삭제
        Long originalPostId = post.getOriginalPostId();
        postRepository.deleteByOriginalPostId(originalPostId);

        // 7) 결과 DTO 반환
        return new PostResponseDTO(post, null, null); // 삭제된 게시글 정보 반환
    }


    private PostResponseDTO deleteQNAPost(Long postId, PostType postType) {
        // 1) 게시글 조회
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new GeneralException(ErrorStatus.POST_NOT_FOUND));

        Long currentUserId = userAuthorizationUtil.getCurrentUserId();
        if (!post.getUser().getId().equals(currentUserId)) {
            throw new GeneralException(ErrorStatus.DELETE_ONLY_ACCESS);
        }

        // 게시글 타입 확인
        if (post.getBoard().getPostType() != postType) {
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        // 2) QnA 게시글일 경우 답변 삭제
        if (postType == PostType.QNA) {
            // 2.1) 답변 조회
            List<Answer> answers = answerRepository.findAllByPostId(postId);

            // 2.2) 각 답변에 대한 이미지 삭제 및 답변 삭제
            for (Answer answer : answers) {
                // 답변 이미지 삭제
                List<AnswerImg> answerImgs = answerImgRepository.findAllByAnswerId(answer.getId());
                for (AnswerImg answerImg : answerImgs) {
                    String s3key = "AnswerImg/" + answerImg.getStoredFileName();
                    amazonS3Manager.deleteFile(s3key);
                    answerImgRepository.delete(answerImg); // AnswerImg 삭제
                }
                answerRepository.delete(answer); // Answer 삭제
            }
        }

        // 3) 관련된 좋아요 정보 삭제
        List<LikeCount> likeCounts = likeCountRepository.findAllByPostId(postId); // 해당 게시글의 좋아요 정보 조회
        likeCountRepository.deleteAll(likeCounts); // 해당 게시글과 관련된 모든 좋아요 삭제

        // 4) S3에서 이미지 삭제 (게시글 이미지 삭제)
        List<PostImg> postImgs = postImgRepository.findByPostId(postId);
        for (PostImg postImg : postImgs) {
            String s3key = "PostImg/" + postImg.getStoredFileName();
            amazonS3Manager.deleteFile(s3key);
            postImgRepository.delete(postImg); // PostImg 삭제
        }

        // 5) 게시글 삭제
        postRepository.delete(post);

        // 6) ALL 및 POPULAR 게시판에서도 해당 게시글 삭제
        Long originalPostId = post.getOriginalPostId();
        postRepository.deleteByOriginalPostId(originalPostId);

        // 7) 결과 DTO 반환
        return new PostResponseDTO(post, null, null); // 삭제된 게시글 정보 반환
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
        if (post.getBoardPosts() == null || post.getBoardPosts().isEmpty()) {
            post.setBoardPosts(new ArrayList<>());  // ✅ 컬렉션이 비어 있으면 초기화
        }

        BoardPost boardPost = new BoardPost();
        boardPost.setBoard(board);
        boardPost.setPost(post);

        board.getBoardPosts().add(boardPost);
        post.getBoardPosts().add(boardPost);  // ✅ 이제 문제 없이 추가 가능

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
    // 전체 게시판 분야 지정 O
    private Post createPostAllByBoardType(BoardRequestDto.AllPost postDTO, User user, Board board) {
        // Crop을 찾고 Post에 연결
        Crop crop = cropRepository.findByName(postDTO.getCrop())
                .orElseThrow(() -> new IllegalArgumentException("작물이 존재하지 않습니다."));

        // Post 생성
        Post post = Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .crop(crop)
                .build();

        return post;
    }


    // 인기 게시판 분야 지정 O
    private Post createPostPopularByBoardType(BoardRequestDto.PopularPost postDTO, User user, Board board) {
        // Crop을 찾고 Post에 연결
        Crop crop = cropRepository.findByName(postDTO.getCrop())
                .orElseThrow(() -> new IllegalArgumentException("작물이 존재하지 않습니다."));

        // Post 생성
        Post post = Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .crop(crop)
                .build();


        return post;
    }


    // Qna,Expert 분야 지정 (상위 분야,하위분야 지정으로 저장)
    private Post createPostByBoardType(BoardRequestDto.QnaPost postDTO, User user, Board board) {
        // Crop을 찾고 Post에 연결
        Crop crop = cropRepository.findByName(postDTO.getCrop())
                .orElseThrow(() -> new IllegalArgumentException("작물이 존재하지 않습니다."));

        // Post 생성
        Post post = Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .crop(crop)
                .build();


        return post;
    }

    private Post createPostByBoardType(BoardRequestDto.ExpertColumn postDTO, User user, Board board) {
        // Crop을 찾고 Post에 연결
        Crop crop = cropRepository.findByName(postDTO.getCrop())
                .orElseThrow(() -> new IllegalArgumentException("작물이 존재하지 않습니다."));

        // Post 생성
        Post post = Post.builder()
                .postTitle(postDTO.getPostTitle())
                .subTitle(postDTO.getSubTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .crop(crop)
                .build();


        return post;
    }
}
