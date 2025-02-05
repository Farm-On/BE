package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.code.status.SuccessStatus;
import com.backend.farmon.domain.Crop;
import com.backend.farmon.domain.Post;
import com.backend.farmon.dto.Answer.AnswerRequestDTO;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.post.PostPagingResponseDTO;
import com.backend.farmon.dto.post.PostRequestDTO;
import com.backend.farmon.dto.post.PostResponseDTO;
import com.backend.farmon.service.AWS.S3Service;
import com.backend.farmon.service.BoardService.BoardServiceImpl;
import com.backend.farmon.service.PostService.PostQueryServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "게시판 페이지", description = "게시판과 관련된 기능을 하는 컨트롤러입니다.")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final BoardServiceImpl boardServiceImpl;
    private final S3Service s3Service;
    private final PostQueryServiceImpl postQueryServiceImpl;

    // 농업인인 경우 자유 게시판 과 Qna에 글을 쓸 수 있음 인증 토큰 자체를 여기에 넣어야 할 거 같다.
    @Operation(
            summary = "자유게시판에서 글을 저장",
            description = "사용자는 자유게시판에서 글을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4002", description = "글이 저장되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))

    })

    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @PostMapping("/free/save")
    public ApiResponse<PostResponseDTO> save_Free_Post(
            @RequestBody BoardRequestDto.FreePost request,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) throws Exception {
        log.info("FreePost에서 request 로 온 정보 "+request.getPostContent());
        PostResponseDTO postResponseDTO=boardServiceImpl.save_FreePost(request, imgList);

        return ApiResponse.onSuccess(postResponseDTO);
    }

    @Operation(
            summary = "Qna글에서 정보를 저장",
            description = "사용자는 Qna글에서 글을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4002", description = "글이 저장되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })

    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false),
    })
    @PostMapping("/qna/save")
    public  ApiResponse<PostResponseDTO>  save_QnA_Post(
            @RequestBody  BoardRequestDto.QnaPost request,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList
    ) throws Exception {
        PostResponseDTO postResponseDTO=boardServiceImpl.save_QnaPost(request, imgList);

        return ApiResponse.onSuccess(postResponseDTO);
    }



    // 전문가인 경우 자유게시판 과 전문가 칼럼에 글을 쓸 수 있음 (위에 꺼에서 추가해야함)

    @Operation(
            summary = "전문가 칼럼 글에서 정보를 저장",
            description = "사용자는 전문가 칼럼 글에서 글을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4002", description = "글이 저장되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })

    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false),
    })
    @PostMapping("/expertCol/save")
    public  ApiResponse<PostResponseDTO> save_exper_Post(
            @RequestBody BoardRequestDto.ExpertColumn request,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList
    ) throws Exception {
        log.info(request.getPostTitle());

        PostResponseDTO postResponseDTO=boardServiceImpl.save_ExperCol(request, imgList);

        return ApiResponse.onSuccess(postResponseDTO);

    }

//    @Operation(
//            summary = "QnA 답변 저장",
//            description = "사용자는 QnA 질문에 대한 답변을 저장할 수 있습니다."
//    )
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4002", description = "글이 저장되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
//
//    })
//    @PostMapping("/qna/answer/save")
//    public ApiResponse<PostResponseDTO> saveQnAAnswer(
//            @RequestParam("userId") Long userId, // 답변자의 ID
//            @RequestParam("questionId") Long questionId, // 질문 ID
//            @RequestBody AnswerRequestDTO answerRequestDTO // 답변 데이터
//    ) {
//        // 게시글 저장 서비스 호출
//        String resultcode = SuccessStatus._OK.getCode();
//        // 성공적인 응답 반환
//        return ApiResponse.onSuccess(SuccessStatus._OK.getCode());
//    }


///// 게시글 목록 그냥 조회 (상세조회X) 리스트 형식으로 돌아옴

    // 인기 게시판 조회
    @GetMapping("/popular/list/{boardId}")
    @Operation(
            summary = "인기게시판에 있는 글을 조회 (페이징)",
            description = "사용자는 인기게시판에 있는 글을 페이징하여 조회할 수 있습니다. boardId는 게시판 종류를 나타냅니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<Page<PostPagingResponseDTO>> getPopularPostByPaging(
            @Parameter(description = "게시판 번호", required = true) @PathVariable Long boardId,
            @Parameter(description = "페이지 번호", required = true) @RequestParam(value = "page") int pageNum,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식 (ASC 또는 DESC)", required = false) @RequestParam(defaultValue = "DESC") String sort,
            @Parameter(description = "필터링조건",required = false) String [] crops
    ) {
        List<String> cropsList = (crops != null) ? Arrays.asList(crops) : Collections.emptyList();
        try {
            // findPopularPosts는 postLike로 내림차순으로 정리
            Page<PostPagingResponseDTO> posts = postQueryServiceImpl.findPopularPosts(boardId ,pageNum, size, sort,cropsList);

            return ApiResponse.onSuccess(posts);
        } catch (Exception e) {
            // 실패 응답 반환 (예: 게시판을 찾을 수 없음)
            return ApiResponse.onFailure( "POST_TYPE4003", "게시판을 찾을 수 없습니다.", null);
        }
    }

    // 전체 게시판에서 자유게시판 ,전문가 칼럼글, QnA 글 등등 전체를 가져옴
    @GetMapping("/all/list/{boardId}")
    @Operation(
            summary = "전체글 조회 (페이징)",
            description = "사용자는 전체글을 페이징하여 조회할 수 있습니다. boardId는 게시판 종류를 나타냅니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<Page<PostPagingResponseDTO>> getAllPostByPaging(
            @Parameter(description = "게시판 번호", required = true) @PathVariable Long boardId,
            @Parameter(description = "페이지 번호", required = true) @RequestParam(value = "page") int pageNum,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식 (ASC 또는 DESC)", required = false) @RequestParam(defaultValue = "DESC") String sort,
            @Parameter(description = "필터링조건",required = false) String [] crops
    )
    {
        List<String> cropsList = (crops != null) ? Arrays.asList(crops) : Collections.emptyList();
        try{
            // 게시판 ID에 해당하는 게시글을 생성일 순으로 정렬하여 페이징 처리
            Page<PostPagingResponseDTO> posts =  postQueryServiceImpl.findAllPostsByBoardPK(boardId, pageNum,size,sort,cropsList);
            return ApiResponse.onSuccess(posts);
        } catch (Exception e) {
            // 실패 응답 반환 (예: 게시판을 찾을 수 없음)
            return ApiResponse.onFailure("POST_TYPE4003", "게시판을 찾을 수 없습니다.", null);
        }

    }



    @GetMapping("/free/list/{boardId}")
    @Operation(
            summary = "자유 게시판 조회 (페이징)",
            description = "사용자는 자유 게시판에 있는 글을  조회할 수 있습니다. boardId는 게시판 종류를 나타냅니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<Page<PostPagingResponseDTO> > get_Free_ByPaging(
            @Parameter(description = "게시판 번호", required = true) @PathVariable Long boardId,
            @Parameter(description = "페이지 번호", required = true) @RequestParam(value = "page") int pageNum,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식 (ASC 또는 DESC)", required = false) @RequestParam(defaultValue = "DESC") String sort,
            @Parameter(description = "필터링조건",required =false) String [] crops
    ) {
        log.info("Crops: " + crops);
        List<String> cropsList = (crops != null) ? Arrays.asList(crops) : Collections.emptyList();
        try{
            // 게시판 ID에 해당하는 게시글을 생성일 순으로 정렬하여 페이징 처리
            Page<PostPagingResponseDTO> posts =  postQueryServiceImpl.findAllPostsByBoardPK(boardId, pageNum,size,sort,cropsList);
            return ApiResponse.onSuccess(posts);
        } catch (Exception e) {
            // 실패 응답 반환 (예: 게시판을 찾을 수 없음)
            return ApiResponse.onFailure("POST_TYPE4003", "게시판을 찾을 수 없습니다.", null);
        }
    }



    @GetMapping("/qna/list/{boardId}")
    @Operation(
            summary = "QnA 게시글 조회 (페이징)",
            description = "사용자는 QnA 게시글을 페이징하여 조회할 수 있습니다. boardNo는 게시판 종류를 나타냅니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse< Page<PostPagingResponseDTO>> getQnaPostByPaging(
            @Parameter(description = "게시판 번호", required = true) @PathVariable Long boardId,
            @Parameter(description = "페이지 번호", required = true) @RequestParam(value = "page") int pageNum,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식 (ASC 또는 DESC)", required = false) @RequestParam(defaultValue = "DESC") String sort,
            @Parameter(description = "필터링조건",required =false) String [] crops
            ) {
        List<String> cropsList = (crops != null) ? Arrays.asList(crops) : Collections.emptyList();
        try {
            // 게시판 ID에 해당하는 게시글을 생성일 순으로 정렬하여 페이징 처리
            Page<PostPagingResponseDTO> posts = postQueryServiceImpl.findQnaPostsByBoardPK(boardId, pageNum, size, sort, cropsList);
            return ApiResponse.onSuccess(posts);
        } catch (Exception e) {
            // 실패 응답 반환 (예: 게시판을 찾을 수 없음)
            return ApiResponse.onFailure("POST_TYPE4003", "게시판을 찾을 수 없습니다.", null);
        }
    }




    @GetMapping("/expertCol/list/{boardId}")
    @Operation(
            summary = "전문가 칼럼 글 조회 (페이징)",
            description = "사용자는 전문가 칼럼 글을 페이징하여 조회할 수 있습니다. boardId는 게시판 종류를 나타냅니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse< Page<PostPagingResponseDTO>> getExpertColPostByPaging(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sort,
            @Parameter(description = "필터링조건",required = false) String [] crops
    ) {
        // 여기는 무조건 작물 정보를 String으로 받아서 처리하거나 boolean으로 처리할수 있는데 일단 해보고 말씀드려보겠습니다.
        List<String> cropsList = (crops != null) ? Arrays.asList(crops) : Collections.emptyList();
        try{
            // 게시판 ID에 해당하는 게시글을 생성일 순으로 정렬하여 페이징 처리
            Page<PostPagingResponseDTO> posts =  postQueryServiceImpl.findExpertsPostsByBoardPK(boardId, pageNum,size,sort,cropsList);
            return ApiResponse.onSuccess(posts);
        } catch (Exception e) {
            // 실패 응답 반환 (예: 게시판을 찾을 수 없음)
            return ApiResponse.onFailure("POST_TYPE4003", "게시판을 찾을 수 없습니다.", null);
        }
    }


    //게시글 상세 조회 (들어가서 내용물을 봄)(PostSummary 만 내용만  페이지수 이런거는 정보 못좀)


    @Operation(
            summary = "인기게시판 글을 상세 조회",
            description = "사용자는 인기게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 번호", required = true),
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = true)
    })
    @GetMapping("popular/list/{postId}/detail")
    public ApiResponse  getPopularPostById( Long boardId,@PathVariable  Long postId) {
        String resultCode;

        PostResponseDTO postDetail = postQueryServiceImpl.getBoardIdAndPostById(boardId,postId);
        resultCode=SuccessStatus._OK.getCode();
        return ApiResponse.onSuccess(resultCode);
    }

    @Operation(
            summary = "전체게시판 글을 상세 조회",
            description = "사용자는 전체게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 번호", required = true),
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = true)
    })
    @GetMapping("all/list/{postId}/detail")
    public ApiResponse  getAllPostById( Long boardId,@PathVariable Long postId) {
        String resultCode;
        PostResponseDTO postDetail = postQueryServiceImpl.getBoardIdAndPostById(boardId,postId);
        resultCode=SuccessStatus._OK.getCode();
        return ApiResponse.onSuccess(resultCode);

    }

    @Operation(
            summary = "자유 게시판 글을 상세 조회",
            description = "사용자는 자유 게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 번호", required = true),
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = true)
    })
    @GetMapping("free/list/{postId}/detail")
    public ApiResponse  getFreePostById( Long boardId,@PathVariable Long postId) {
        String resultCode;
        PostResponseDTO postDetail = postQueryServiceImpl.getBoardIdAndPostById(boardId,postId);
        resultCode=SuccessStatus._OK.getCode();
        return ApiResponse.onSuccess(resultCode);
    }


    @Operation(
            summary = "QnA 게시판 글을 상세 조회",
            description = "사용자는 QnA 게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 번호", required = true),
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = true)
    })
    @GetMapping("qna/list/{postId}/detail")
    public ApiResponse getQnaPostById(Long boardId,@PathVariable  Long postId) {
        String resultCode;
        PostResponseDTO postDetail = postQueryServiceImpl.getBoardIdAndPostById(boardId,postId);
        resultCode=SuccessStatus._OK.getCode();
        return ApiResponse.onSuccess(resultCode);
    }



    @Operation(
            summary = "전문가 칼럼 게시판 글을 상세 조회",
            description = "사용자는 전문가 칼럼 게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 번호", required = true),
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = true)
    })
    @GetMapping("expertCol/list/{postId}/detail")
    public ApiResponse getExpertColumnPostById( Long boardId,@PathVariable Long postId) {
        String resultCode;
        PostResponseDTO postDetail = postQueryServiceImpl.getBoardIdAndPostById(boardId,postId);
        resultCode=SuccessStatus._OK.getCode();
        return ApiResponse.onSuccess(resultCode);
    }

}
