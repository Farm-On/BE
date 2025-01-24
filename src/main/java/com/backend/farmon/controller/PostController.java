package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.SuccessStatus;
import com.backend.farmon.dto.Answer.AnswerRequestDTO;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.Filter.FieldCategoryDTO;
import com.backend.farmon.dto.post.PostRequestDTO;
import com.backend.farmon.dto.post.PostResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "게시판 페이지", description = "게시판과 관련된 기능을 하는 컨트롤러입니다.")
@Controller
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {



    @Operation(
            summary = "인기글에서 정보를 저장",
            description = "사용자는 인기글에서 글을 저장할 수 있습니다."
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
            @Parameter(name = "BoardRequestDTO", description = "게시판 유형"),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @PostMapping("/popular/save")
    public ApiResponse save_Popular_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.PopularPost request,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        String resultcode = SuccessStatus._OK.getCode();


        return ApiResponse.onSuccess(resultcode);
    }

    @Operation(
            summary = "전체글에서 정보를 저장",
            description = "사용자는 전체글에서 글을 저장할 수 있습니다."
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
            @Parameter(name = "BoardRequestDTO", description = "게시판 유형"),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)

    })
    @PostMapping("/all/save")
    public ApiResponse save_All_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestPart(value = "posting") @Valid BoardRequestDto.AllPost request,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        String resultcode = SuccessStatus._OK.getCode();

        // userId, request, imgList를 처리하는 로직을 추가할 수 있음


        return ApiResponse.onSuccess(resultcode);
    }


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
            @Parameter(name = "BoardRequestDTO", description = "게시판 유형"),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @PostMapping("/free/save")
    public ApiResponse save_Free_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.FreePost request,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        String resultcode = SuccessStatus._OK.getCode();


        return ApiResponse.onSuccess(resultcode);
    }

    ///////////
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
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false),
            @Parameter(name="Filter" ,description = "분야(필터)",required = true)
    })
    @PostMapping("/qna/save")
    public ApiResponse save_QnA_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.QnaPost request,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList,
            @RequestBody FieldCategoryDTO filterCategoryDTO ) {

        // 게시글 저장 서비스 호출
        String resultcode = SuccessStatus._OK.getCode();

        // 성공적인 응답 반환
        return ApiResponse.onSuccess(resultcode);
    }



    @Operation(
            summary = "QnA 답변 저장",
            description = "사용자는 QnA 질문에 대한 답변을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4002", description = "글이 저장되지 않았습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))

    })
    @PostMapping("/qna/answer/save")
    public ApiResponse saveQnAAnswer(
            @RequestParam("userId") Long userId, // 답변자의 ID
            @RequestParam("questionId") Long questionId, // 질문 ID
            @RequestBody AnswerRequestDTO answerRequestDTO // 답변 데이터
    ) {
        // 게시글 저장 서비스 호출
        String resultcode = SuccessStatus._OK.getCode();
        // 성공적인 응답 반환
        return ApiResponse.onSuccess(SuccessStatus._OK.getCode());
    }

    //삭제는 구현X

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
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false),
            @Parameter(name="Filter" ,description = "분야(필터)",required = true)
    })
    @PostMapping("/expertCol/save")
    public ApiResponse save_exper_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.ExpertColumn request,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList,
            @RequestBody FieldCategoryDTO filterCategoryDTO) {

        // 게시글 저장 서비스 호출
        String resultcode = SuccessStatus._OK.getCode();

        // 성공적인 응답 반환
        return ApiResponse.onSuccess(resultcode);
    }


///// 게시글 목록 그냥 조회 (상세조회X) 리스트 형식으로 돌아옴


    @GetMapping("/popular/list/{boardId}")
    @Operation(
            summary = "인기글 조회 (페이징)",
            description = "사용자는 인기글을 페이징하여 조회할 수 있습니다. boardNo는 게시판 종류를 나타냅니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4003", description = "게시판을 찾을 수 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<List<PostResponseDTO>> getPopularPostByPaging(
            @Parameter(description = "게시판 번호", required = true) @PathVariable Long boardId,
            @Parameter(description = "페이지 번호", required = true) @RequestParam(value = "page") int pageNum,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식 (ASC 또는 DESC)", required = false) @RequestParam(defaultValue = "DESC") String sort
    ) {
        // 로직 구현
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        return ApiResponse.onSuccess(postResponseDTOList);
    }

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
    public ApiResponse<List<PostResponseDTO>> getAllPostByPaging(
            @Parameter(description = "게시판 번호", required = true) @PathVariable Long boardId,
            @Parameter(description = "페이지 번호", required = true) @RequestParam(value = "page") int pageNum,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "정렬 방식 (ASC 또는 DESC)", required = false) @RequestParam(defaultValue = "DESC") String sort
          ){
        // 로직 구현
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        return ApiResponse.onSuccess(postResponseDTOList);
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
    public ApiResponse<List<PostResponseDTO>> get_Free_ByPaging(
            @Parameter(description = "게시판 번호", required = true) @PathVariable Long boardId,
            @Parameter(description = "페이지 번호", required = true) @RequestParam(value = "page") int pageNum,
            @Parameter(description = "페이지 크기", required = false) @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "정렬 방식 (ASC 또는 DESC)", required = false) @RequestParam(defaultValue = "DESC") String sort
    ) {
        // 로직 구현
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        return ApiResponse.onSuccess(postResponseDTOList);
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
    public ApiResponse<List<PostResponseDTO>> getQnaPostByPaging(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "DESC") String sort,
            @RequestBody FieldCategoryDTO filterCondition ) {
        // 로직 구현
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        return ApiResponse.onSuccess(postResponseDTOList);
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
    public ApiResponse<List<PostResponseDTO>> getExpertColPostByPaging(
            @PathVariable Long boardId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "DESC") String sort,
            @RequestBody FieldCategoryDTO filterCondition
    ) {
        // 로직 구현
        List<PostResponseDTO> postResponseDTOList = new ArrayList<>();
        return ApiResponse.onSuccess(postResponseDTOList);
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
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("popular/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getPopularPostById(@PathVariable  Long postId) {

        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
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
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("all/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getAllPostById(@PathVariable Long postId) {


        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
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
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("free/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getFreePostById(@PathVariable Long postId) {
        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
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
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("qna/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getQnaPostById(@PathVariable  Long postId) {
        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
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
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("expertCol/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getExpertColumnPostById(@PathVariable Long postId) {
        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
    }

}



