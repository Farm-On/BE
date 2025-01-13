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
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @PostMapping("/all/save")
    public ApiResponse save_All_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestPart(value = "posting") @Valid BoardRequestDto.AllPost request,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        // userId, request, imgList를 처리하는 로직을 추가할 수 있음

        return new ApiResponse(true, resultcode, resultmsg);
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
            @Parameter(name = "postRequestDTO", description = "게시글 정보", required = true),
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @GetMapping("/popular/save")
    public ApiResponse save_Popular_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.PopularPost request,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        // 성공적인 응답 반환
        return new ApiResponse(true, resultcode, resultmsg);
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
            @Parameter(name = "imgList", description = "첨부된 이미지 목록 (optional)", required = false)
    })
    @GetMapping("/qna/save")
    public ApiResponse<PostRequestDTO> save_QnA_Post(
            @RequestParam("userId") Long userId, // userId를 추가
            @RequestParam(name = "posting") BoardRequestDto.QnaPost request,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList) {

        // 게시글 저장 서비스 호출
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        // 성공적인 응답 반환
        return new ApiResponse(true, resultcode, resultmsg);
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
            @Parameter(name = "areaId", description = "게시글 필터링을 위한 분야 ID", required = false),
            @Parameter(name = "locationId", description = "게시글 필터링을 위한 위치 ID", required = false)
    })
    @PostMapping("/expertLounge/save")
    public ApiResponse<PostRequestDTO> Expert_Post(
            @RequestParam(name = "userId") Long userId,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgLiPostFilterDTO,
            @RequestBody(required = false) FieldCategoryDTO Fielddto,
            @RequestBody(required = false) LocationFilterDTO locatinDTO
    ) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        // 필터링 로직을 postFilterDTO에서 받아서 처리할 수 있습니다.
        // 예: areaId나 locationId 값에 따라 필터링 처리


        return new ApiResponse(true, resultcode, resultmsg);
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
    public ApiResponse<PostRequestDTO> ExpertCol_Post(
            @RequestParam(name = "userId") Long userId,
            @RequestBody PostRequestDTO postRequestDTO,
            @RequestPart(value = "imgList", required = false) List<MultipartFile> imgList,
            @RequestBody(required = false) AllFilterDTO Fielddto,
            @RequestBody(required = false) KnowHowFilterDTO locatinDTO
    ) {
        // 필터링 로직을 postFilterDTO에서 받아서 처리할 수 있습니다.
        // 예: areaId나 locationId 값에 따라 필터링 처리
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return ApiResponse.onSuccess(postRequestDTO);
    }

///// 게시글 목록 그냥 조회 (상세조회X)

    @GetMapping("/all/list/{boardNo}")
    @Operation(
            summary = "전체글에서 정보를 조회",
            description = "사용자는 전체글에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "pageNum", description = "페이지 번호", required = true),
            @Parameter(name = "size", description = "페이지 크기", required = false),
            @Parameter(name = "sort", description = "정렬 방식", required = false)
    })
    public ApiResponse getAllPostByPaging(
            @PathVariable Long boardNo,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "DESC") String sort) {


        // 여기서 boardNo, userId, pageNum, size, sort 등을 사용하여 데이터 처리 로직 작성
        List<PostResponseDTO> posts;

        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

    @GetMapping("/popular/list/{boardNo}")
    @Operation(
            summary = "전체글에서 정보를 조회",
            description = "사용자는 전체글에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "pageNum", description = "페이지 번호", required = true),
            @Parameter(name = "size", description = "페이지 크기", required = false),
            @Parameter(name = "sort", description = "정렬 방식", required = false)
    })
    public ApiResponse getpopularPostByPaging(
            @PathVariable Long boardNo,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "DESC") String sort) {


        // 여기서 boardNo, userId, pageNum, size, sort 등을 사용하여 데이터 처리 로직 작성
        List<PostResponseDTO> posts;

        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }


    @GetMapping("/qna/list/{boardNo}")
    @Operation(
            summary = "질문 게시판에서 정보를 조회",
            description = "사용자는 질문 게시판에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "pageNum", description = "페이지 번호", required = true),
            @Parameter(name = "size", description = "페이지 크기", required = false),
            @Parameter(name = "sort", description = "정렬 방식", required = false),
            @Parameter(name = "isAnswered", description = "질문 완료 여부", required = false)
    })
    public ApiResponse<List<PostResponseDTO>> getQnaPostByPaging(
            @PathVariable Long boardNo,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "DESC") String sort,
            @RequestParam(value = "isAnswered", required = false) Boolean isAnswered) {

        // 질문 완료 여부에 따라 게시글 필터링
        List<PostResponseDTO> posts;

        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }


    @GetMapping("/expertLounge/list/{boardNo}")
    @Operation(
            summary = "전문가 라운지에서 정보를 조회",
            description = "사용자는 전문가 라운지에서 글을 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "pageNum", description = "페이지 번호", required = true),
            @Parameter(name = "size", description = "페이지 크기", required = false),
            @Parameter(name = "sort", description = "정렬 방식", required = false),
            @Parameter(name = "FielCategorydDTO", description = "페이지 크기", required = false),
            @Parameter(name = "LocationFilerDTO", description = "정렬 방식", required = false)
    })

    public ApiResponse getexpertLoungePostByPaging(
            @PathVariable Long boardNo,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "DESC") String sort,
            @RequestParam(value = "FielCategorydDTO", required = false) FieldCategoryDTO FieldFilterDTO,
            @RequestParam(value = "LocationFilerDTO", required = false) LocationFilterDTO LocationFilterDTO) {
        // 여기서 boardNo, userId, pageNum, size, sort 등을 사용하여 데이터 처리 로직 작성
        List<PostResponseDTO> posts;

        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);

    }

    @Operation(
            summary = "Expert 칼럼 글을 조회",
            description = "사용자는 Expert 칼럼 글을 필터링하여 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "pageNum", description = "페이지 번호 (기본값: 1)", required = false),
            @Parameter(name = "size", description = "한 페이지에 표시할 게시글 수 (기본값: 20)", required = false),
            @Parameter(name = "sort", description = "정렬 방식 (기본값: DESC)", required = false),
            @Parameter(name = "AllFilterDTO", description = "페이지 크기", required = false),
            @Parameter(name = "KnowHowFilterDTO", description = "정렬 방식", required = false)

    })
    @GetMapping("/expertCol/list/{boardNo}")
    public ApiResponse  getexpertColPostByPaging(
            @PathVariable Long boardNo,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "DESC") String sort,
            @RequestParam(value = "AllFilterDTO", required = false) AllFilterDTO  AllFilterDTO,
            @RequestParam(value = "KnowHowFilterDTO", required = false) KnowHowFilterDTO KnowHowDTO)
     {
        // 필터링 로직을 postFilterDTO에서 받아서 처리할 수 있습니다.
        // 예: areaId나 locationId 값에 따라 필터링 처리
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);

    }
    //게시글 상세 조회


    @Operation(
            summary = "전체게시판 글을 상세 조회",
            description = "사용자는 전체게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시글 번호", required = false)
    })
    @GetMapping("all/list/{postNo}/detail")
    public ApiResponse getAllPostById(@PathVariable Long postNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

    @Operation(
            summary = "인기게시판 글을 상세 조회",
            description = "사용자는 인기게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시글 번호", required = false)
    })
    @GetMapping("popular/list/{postNo}/detail")
    public ApiResponse getPopularPostById(@PathVariable Long postNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

    @Operation(
            summary = "QnA 게시판 글을 상세 조회",
            description = "사용자는 QnA 게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시글 번호", required = false)
    })
    @GetMapping("qna/list/{postNo}/detail")
    public ApiResponse getQnaPostById(@PathVariable Long postNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        //답변 완료 로직

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

    @Operation(
            summary = "전문가 라운지 게시판 글을 상세 조회",
            description = "사용자는 전문가 라운지 게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시글 번호", required = false)
    })
    @GetMapping("expertLounge/list/{postNo}/detail")
    public ApiResponse getExpertLoungePostById(@PathVariable Long postNo,
                                               @RequestBody FieldCategoryDTO fieldCategoryDTO,
                                               @RequestBody LocationFilterDTO locationDTO) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

    @Operation(
            summary = "전문가 칼럼 게시판 글을 상세 조회",
            description = "사용자는 전문가 칼럼 게시판 글을 상세 조회할 수 있습니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시글 번호", required = false)
    })
    @GetMapping("expertCol/list/{postNo}/detail")
    public ApiResponse getExpertColumnPostById(@PathVariable Long postNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

}



