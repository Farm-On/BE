package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.SuccessStatus;
import com.backend.farmon.dto.Board.BoardRequestDto;
import com.backend.farmon.dto.FieldFilterDTO.AllFilterDTO;
import com.backend.farmon.dto.FieldFilterDTO.FieldCategoryDTO;
import com.backend.farmon.dto.FieldFilterDTO.KnowHowFilterDTO;
import com.backend.farmon.dto.FieldFilterDTO.LocationFilterDTO;
import com.backend.farmon.dto.PostForm.PostRequestDTO;
import com.backend.farmon.dto.PostForm.PostResponseDTO;
import com.backend.farmon.dto.like.LikeRequestDTO;
import com.backend.farmon.dto.like.LikeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "게시판 페이지", description = "게시판과 관련된 기능을 하는 컨트롤러입니다.")
@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {


    @Operation(
            summary = "전체글에서 정보를 저장",
            description = "사용자는 전체글에서 글을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name="BoardRequestDTO" ,description = "게시판 유형"),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)

    })
    @PostMapping("/all/save")
    public ApiResponse save_All_Post(
            @RequestParam("userId") String userId, // userId를 추가
            @RequestPart(value = "posting") @Valid BoardRequestDto.AllPost request,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        // userId, request, imgList를 처리하는 로직을 추가할 수 있음


        return ApiResponse.onSuccess(resultcode);
    }

    @Operation(
            summary = "인기글에서 정보를 저장",
            description = "사용자는 인기글에서 글을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })

    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name="BoardRequestDTO" ,description = "게시판 유형"),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @PostMapping("/popular/save")
    public ApiResponse save_Popular_Post(
            @RequestParam("userId") String userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.PopularPost request,
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })

    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @PostMapping("/qna/save")
    public ApiResponse save_QnA_Post(
            @RequestParam("userId") String userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.QnaPost request,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        // 게시글 저장 서비스 호출
        String resultcode = SuccessStatus._OK.getCode();

        // 성공적인 응답 반환
        return ApiResponse.onSuccess(resultcode);
    }


    @Operation(
            summary = "Expert 라운지 글에서 정보를 저장",
            description = "사용자는 Expert 라운지 글에서 글을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false),
            @Parameter(name = "FieldCategoryId", description = "게시글 필터링을 위한 분야 ID", required = false),
            @Parameter(name = "locationFilterId", description = "게시글 필터링을 위한 위치 ID", required = false)
    })
    @PostMapping("/expertLounge/save")
    public ApiResponse Expert_Post(
            @RequestParam(name = "userId") String userId,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgLiPostFilterDTO,
            @RequestBody(required = false) FieldCategoryDTO Fielddto,
            @RequestBody(required = false) LocationFilterDTO locatinDTO
    ) {
        String resultcode = SuccessStatus._OK.getCode();


        return ApiResponse.onSuccess(resultcode);
    }


    @Operation(
            summary = "Expert 칼럼 글에서 정보를 저장",
            description = "사용자는 Expert 칼럼 글에서 글을 저장할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", required = true),
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false),
            @Parameter(name = "AllId", description = "게시글 필터링을 위한 전체 ID", required = false),
            @Parameter(name = "KnowHowId", description = "게시글 필터링을 위한 노하우 ID", required = false)
    })
    @PostMapping("/expertCol/save")
    public ApiResponse  ExpertCol_Post(
            @RequestParam(name = "userId") String userId,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList,
            @RequestBody(required = false) AllFilterDTO AllFilterdto,
            @RequestBody(required = false) KnowHowFilterDTO KnowHowFilterDTO
    ) {
        // 필터링 로직을 postFilterDTO에서 받아서 처리할 수 있습니다.
        // 예: areaId나 locationId 값에 따라 필터링 처리
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return ApiResponse.onSuccess(resultcode);
    }

///// 게시글 목록 그냥 조회 (상세조회X) 리스트 형식으로 돌아옴

    @GetMapping("/all/list/{boardNo}")
    @Operation(
            summary = "전체글에서 정보를 조회 boardNo 는 게시판 종류 (상세 조회 아님)",
            description = "사용자는 전체글에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 종류", required = true),
            @Parameter(name = "PostRequesDTO" ,description = "게시글내용(제목,내용,조회수,좋아요수)와 페이지수 ,글 몇개",required = true)
    })
    public ApiResponse<List<PostResponseDTO>> getAllPostByPaging(
            @PathVariable String boardId,
            @RequestBody PostRequestDTO postRequestDTO
            ) {

        // Convert the Post entities to PostResponseDTO
        List<PostResponseDTO> postResponseDTOList=new ArrayList<>();

        return ApiResponse.onSuccess(postResponseDTOList);
    }

    @GetMapping("/popular/list/{boardId}")
    @Operation(
            summary = "전체글에서 정보를 조회(상세 조회 아님)",
            description = "사용자는 전체글에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 종류", required = true),
            @Parameter(name = "PostRequesDTO" ,description = "게시글내용(제목,내용,조회수,좋아요수)와 페이지수 ,글 몇개",required = true)
    })
    public ApiResponse<List<PostResponseDTO>> getpopularPostByPaging(
            @PathVariable String boardId,
            @RequestBody PostRequestDTO postRequestDTO) {


        // Convert the Post entities to PostResponseDTO
        List<PostResponseDTO> postResponseDTOList=new ArrayList<>();

        return ApiResponse.onSuccess(postResponseDTOList);
    }


    @GetMapping("/qna/list/{boardId}")
    @Operation(
            summary = "질문 게시판에서 정보를 조회(상세 조회 아님)",
            description = "사용자는 질문 게시판에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 종류", required = true),
            @Parameter(name = "PostRequesDTO" ,description = "게시글내용(제목,내용,조회수,좋아요수)와 페이지수 ,글 몇개",required = true),
            @Parameter(name = "isAnswered", description = "질문 완료 여부", required = false)
    })
    public ApiResponse<List<PostResponseDTO>> getQnaPostByPaging(
            @PathVariable String boardId,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestParam(value = "isAnswered", required = false) Boolean isAnswered) {

        // Convert the Post entities to PostResponseDTO
        List<PostResponseDTO> postResponseDTOList=new ArrayList<>();

        return ApiResponse.onSuccess(postResponseDTOList);
    }


    @GetMapping("/expertLounge/list/{boardId}")
    @Operation(
            summary = "전문가 라운지에서 글를 조회 (상세 조회 아님)",
            description = "사용자는 전문가 라운지에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "boardId", description = "게시판 종류", required = true),
            @Parameter(name = "PostRequesDTO" ,description = "게시글내용(제목,내용,조회수,좋아요수)와 페이지수 ,글 몇개",required = true),
            @Parameter(name = "FielCategorydDTO", description = "페이지 크기", required = false),
            @Parameter(name = "LocationFilerDTO", description = "정렬 방식", required = false)
    })

    public ApiResponse <List<PostResponseDTO>>getexpertLoungePostByPaging(
            @PathVariable String boardId,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestParam(value = "FielCategorydDTO", required = false) FieldCategoryDTO FieldFilterDTO,
            @RequestParam(value = "LocationFilerDTO", required = false) LocationFilterDTO LocationFilterDTO) {

        // Convert the Post entities to PostResponseDTO
        List<PostResponseDTO> postResponseDTOList=new ArrayList<>();

        return ApiResponse.onSuccess(postResponseDTOList);

    }

    @Operation(
            summary = "Expert 칼럼 글을 조회(상세 조회 아님)",
            description = "사용자는 Expert 칼럼 글을 필터링하여 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "PostRequesDTO" ,description = "게시글내용(제목,내용,조회수,좋아요수)와 페이지수 ,글 몇개",required = true),
            @Parameter(name = "AllFilterDTO", description = "페이지 크기", required = false),
            @Parameter(name = "KnowHowFilterDTO", description = "정렬 방식", required = false)

    })
    @GetMapping("/expertCol/list/{boardId}")
    public ApiResponse<List<PostResponseDTO>>  getexpertColPostByPaging(
            @PathVariable String boardId,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestParam(value = "AllFilterDTO", required = false) AllFilterDTO  AllFilterDTO,
            @RequestParam(value = "KnowHowFilterDTO", required = false) KnowHowFilterDTO KnowHowDTO)
     {

         // Convert the Post entities to PostResponseDTO
         List<PostResponseDTO> postResponseDTOList=new ArrayList<>();

         return ApiResponse.onSuccess(postResponseDTOList);

    }
    //게시글 상세 조회 (들어가서 내용물을 봄)(PostSummary 만 내용만  페이지수 이런거는 정보 못좀)


    @Operation(
            summary = "전체게시판 글을 상세 조회",
            description = "사용자는 전체게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("all/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getAllPostById(@PathVariable String postId) {


        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
    }


    @Operation(
            summary = "인기게시판 글을 상세 조회",
            description = "사용자는 인기게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("popular/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getPopularPostById(@PathVariable  String postId) {

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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("qna/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getQnaPostById(@PathVariable  String postId) {
        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
    }

    @Operation(
            summary = "전문가 라운지 게시판 글을 상세 조회",
            description = "사용자는 전문가 라운지 게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("expertLounge/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getExpertLoungePostById(@PathVariable  String postId,
                                               @RequestBody FieldCategoryDTO fieldCategoryDTO,
                                               @RequestBody LocationFilterDTO locationDTO) {
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시글 작성한 사람 Id", required = false)
    })
    @GetMapping("expertCol/list/{postId}/detail")
    public ApiResponse<PostResponseDTO.PostSummary> getExpertColumnPostById(@PathVariable String postId) {
        // Post 객체를 PostResponseDTO.PostSummary로 변환
        PostResponseDTO.PostSummary postSummary = PostResponseDTO.PostSummary.builder()
                .build();

        // 성공 응답 반환
        return ApiResponse.onSuccess(postSummary);
    }

}



