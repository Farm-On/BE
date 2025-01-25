package com.backend.farmon.service.BoardService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.GeneralException;
import com.backend.farmon.domain.Board;
import com.backend.farmon.domain.Post;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.Filter.FieldCategory;
import com.backend.farmon.dto.Filter.FieldCategoryDTO;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.repository.BoardRepository.BoardRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.backend.farmon.domain.QPost.post;

@Service
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository; // 게시글 데이터를 저장하기 위한 Repository
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3Service s3Service; // 파일 업로드를 위한 S3 서비스



    @Override
    public void save_AllPost(BoardRequestDto.AllPost postDto, List<MultipartFile> multipartFiles) throws Exception {
        User user =userRepository.findById(postDto.getUserId())
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board=boardRepository.findById(postDto.getBoardId())
                .orElseThrow(()->new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if(board.getPostType() != PostType.ALL){
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);

    }

    @Override
    public void save_FreePost(BoardRequestDto.FreePost postDto, List<MultipartFile> multipartFiles) throws Exception {
        User user =userRepository.findById(postDto.getUserId())
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board=boardRepository.findById(postDto.getBoardId())
                .orElseThrow(()->new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if(board.getPostType() != PostType.ALL){
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);
    }

    @Override
    public void save_PopularPost(BoardRequestDto.PopularPost postDto, List<MultipartFile> multipartFiles) throws Exception {
        User user =userRepository.findById(postDto.getUserId())
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board=boardRepository.findById(postDto.getBoardId())
                .orElseThrow(()->new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if(board.getPostType() != PostType.ALL){
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);
    }
    // 분야 필수 에러 전문가 칼럼 과 qna 게시판에 추가
    @Override
    public void save_QnaPost(BoardRequestDto.QnaPost postDto, List<MultipartFile> multipartFiles) throws Exception {
        validateFieldCategory(postDto.getFieldCategory());
        User user =userRepository.findById(postDto.getUserId())
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board=boardRepository.findById(postDto.getBoardId())
                .orElseThrow(()->new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if(board.getPostType() != PostType.ALL){
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);

    }
    // 분야 필수 에러 전문가 칼럼 과 qna 게시판에 추가
    @Override
    public void save_ExperCol(BoardRequestDto.ExpertColumn postDto, List<MultipartFile> multipartFiles) throws Exception {
        validateFieldCategory(postDto.getFieldCategory());
        User user =userRepository.findById(postDto.getUserId())
                .orElseThrow(()->new GeneralException(ErrorStatus.USER_NOT_FOUND));
        Board board=boardRepository.findById(postDto.getBoardId())
                .orElseThrow(()->new GeneralException(ErrorStatus.POST_NOT_FOUND));

        if(board.getPostType() != PostType.ALL){
            throw new GeneralException(ErrorStatus.POST_NOT_FOUND);
        }

        Post post = createPostByBoardType(postDto, user, board);
        postRepository.save(post);
        if (multipartFiles != null && !multipartFiles.isEmpty()) {
            for (MultipartFile file : multipartFiles) {
                s3Service.saveImage(file, post);
            }
        }
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
        Locale.setDefault(Locale.KOREA);
    }

     // QnA 및 전문가 칼럼에 필요한 FieldCategory 유효성 검증
    private void validateFieldCategory(FieldCategory fieldCategory) {
        if (fieldCategory == null) {
            throw new IllegalArgumentException("분야 카테고리는 필수 입력 값입니다.");
        }
    }


    private Post createPostByBoardType(BoardRequestDto.AllPost postDTO,User user,Board board) throws Exception {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .build();
    }

    private Post createPostByBoardType(BoardRequestDto.PopularPost postDTO,User user,Board board) throws Exception {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .build();
    }

    private Post createPostByBoardType(BoardRequestDto.FreePost postDTO,User user,Board board) throws Exception {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .user(user)
                .board(board)
                .build();
    }

    private Post createPostByBoardType(BoardRequestDto.QnaPost postDTO,User user,Board board) throws Exception {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .fieldCategory(postDTO.getFieldCategory())
                .user(user)
                .board(board)
                .build();
    }

    private Post createPostByBoardType(BoardRequestDto.ExpertColumn postDTO,User user,Board board) throws Exception {
        return Post.builder()
                .postTitle(postDTO.getPostTitle())
                .postContent(postDTO.getPostContent())
                .fieldCategory(postDTO.getFieldCategory())
                .user(user)
                .board(board)
                .build();
    }


}
