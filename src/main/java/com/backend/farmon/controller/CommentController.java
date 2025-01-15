package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.SuccessStatus;
import com.backend.farmon.dto.Comment.CommenResponseDTO;
import com.backend.farmon.dto.Comment.CommentRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@Tag(name = "댓글 페이지", description = "댓글에 관한 API")
@Controller
@RequestMapping("/api/posts/{postId}/comments")
public class CommentController {

    @Operation(summary = "댓글 저장", description = "사용자가 그냥 부모 댓글(댓글)을 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
            @Parameter(name = "CommentRequestDTO", description = "댓글 정보", required = true),
    })
    @PostMapping("/save")
    public ApiResponse saveComment(@PathVariable Long postId, @RequestBody @Valid CommentRequestDTO request) {
        String resultcode = SuccessStatus._OK.getCode();

        return ApiResponse.onSuccess(resultcode);
    }


    @Operation(summary = "대댓글 저장", description = "사용자가 대댓글을 저장합니다..")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
            @Parameter(name = "commentNo", description = "부모댓글 저장", required = true),
            @Parameter(name = "CommentSaveChildRequestDto", description = "대댓글 정보", required = true),
    })
    @PostMapping("/{commentNo}/save")
    public ApiResponse saveChildComment(@PathVariable Long postId, @PathVariable Long commentNo, @RequestBody @Valid CommentRequestDTO.CommentSaveChildRequestDto request) {
        String resultcode = SuccessStatus._OK.getCode();

        return ApiResponse.onSuccess(resultcode);
    }





    ///// 조회

    @Operation(summary = "부모 댓글 조회", description = "사용자가 댓글(부모 댓글)을 조회합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
            })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
            })
    @GetMapping
    public ApiResponse<CommenResponseDTO.CommentParentReadResponseDto> getParentComments(@PathVariable String postId) {

        CommenResponseDTO.CommentParentReadResponseDto dto=  CommenResponseDTO.CommentParentReadResponseDto.builder().build();

        return ApiResponse.onSuccess(dto);
    }



    @Operation(summary = "특정 댓글의 대댓글(자식 댓글) 조회", description = "사용자가 부모 댓글의 대댓글(자식 댓글) 조회합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
            @Parameter(name = "commentNo", description = "부모댓글 번호", required = true),
    })
    @GetMapping("/{commentNo}/children")
    public ApiResponse <CommenResponseDTO.CommentChildReadResponseDto> getChild (@PathVariable Long postId, @PathVariable Long commentNo) {

        CommenResponseDTO.CommentChildReadResponseDto dto=  CommenResponseDTO.CommentChildReadResponseDto.builder().build();

        return ApiResponse.onSuccess(dto);
    }



    /**
     * 게시글의 모든 댓글 조회 (부모 + 자식 모두)
     */
    @Operation(summary = "게시글의 모든 댓글 조회", description = "사용자가 게시글의 모든 댓글 조회합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
    })
    @GetMapping("/all")
    public ApiResponse<List<CommenResponseDTO>> getAllComments(@PathVariable Long postId) {

        // 댓글 목록 조회 로직
        List<CommenResponseDTO> comments=new ArrayList<>();

        // 조회된 댓글 목록을 응답으로 반환
        return ApiResponse.onSuccess(comments);

    }


    @Operation(summary = "게시글의 부모 댓글(댓글) 수정", description = "사용자가 게시글의 부모 댓글 수정합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
            @Parameter(name = "commentNo", description = "부모댓글번호", required = true),
            @Parameter(name = "CommentUpdateRequestDto", description = "댓글내용 수정", required = true),
    })
    @PutMapping("/{commentNo}")
    public ApiResponse<CommenResponseDTO.CommentParentReadResponseDto> updateComment(@PathVariable Long postId,@PathVariable Long commentNo, @RequestBody @Valid CommentRequestDTO.CommentUpdateRequestDto request) {
        CommenResponseDTO.CommentParentReadResponseDto dto=  CommenResponseDTO.CommentParentReadResponseDto.builder().build();

        return ApiResponse.onSuccess(dto);
    }




    @Operation(summary = "게시글의 대댓글  수정", description = "사용자가 게시글의 대댓글 수정합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
            @Parameter(name = "commentNo", description = "부모댓글 번호", required = true),
            @Parameter(name = "ChildNo", description = "대 댓글 번호", required = true),
            @Parameter(name = "CommentUpdateRequestDto", description = "댓글내용 수정", required = true),

    })
    @PutMapping("/{commentNo}/children/{ChildNo}")
    public ApiResponse updateChildComment(@PathVariable Long postId, @PathVariable Long commentNo,@PathVariable Long ChildNo,
                                          @RequestBody @Valid CommentRequestDTO.CommentUpdateRequestDto request) {

        CommenResponseDTO.CommentChildReadResponseDto dto=  CommenResponseDTO.CommentChildReadResponseDto.builder().build();

        return ApiResponse.onSuccess(dto);
    }


    @Operation(summary = "게시글의 댓글  전부 삭제", description = "댓글 전부 삭제 API")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postId", description = "게시물 작성한 사람 ID", required = true),
            @Parameter(name = "commentNo", description = "부모댓글 번호", required = true),
    })
    /**
     * 댓글 삭제 (댓글과 대댓글 모두 포함) 부모댓글 삭제 만 포함시켰음
     */
    @DeleteMapping("/{commentNo}")
    public ApiResponse deleteComment(@PathVariable Long postId, @PathVariable Long commentNo) {

        String resultcode = SuccessStatus._OK.getCode();

        return ApiResponse.onSuccess(resultcode);
    }


}
