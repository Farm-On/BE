package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.SuccessStatus;
import com.backend.farmon.dto.Comment.CommentRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Tag(name = "댓글 페이지", description = "댓글에 관한 API")
@Controller
@RequestMapping("/posts/{postNo}/comments")
public class CommentController {

    @Operation(summary = "댓글 저장", description = "사용자가 그냥 부모 댓글(댓글)을 저장합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시물번호", required = true),
            @Parameter(name = "CommentRequestDTO", description = "댓글 정보", required = true),
    })
    @PostMapping("/save")
    public ApiResponse saveComment(@PathVariable Long postNo, @RequestBody @Valid CommentRequestDTO request) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }


    @Operation(summary = "대댓글 저장", description = "사용자가 대댓글을 저장합니다..")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시물번호", required = true),
            @Parameter(name = "parentNo", description = "부모댓글저장", required = true),
            @Parameter(name = "CommentSaveChildRequestDto", description = "대댓글 정보", required = true),
    })
    @PostMapping("/{parentNo}/save")
    public ApiResponse saveChildComment(@PathVariable Long postNo, @PathVariable Long parentNo, @RequestBody @Valid CommentRequestDTO.CommentSaveChildRequestDto request) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }


    @Operation(summary = "부모 댓글 조회", description = "사용자가 댓글(부모 댓글)을 조회합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
            })
    @Parameters({
            @Parameter(name = "postNo", description = "게시물번호", required = true),
            })
    @GetMapping
    public ApiResponse getParentComments(@PathVariable Long postNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }



    @Operation(summary = "특정 댓글의 대댓글(자식 댓글) 조회", description = "사용자가 부모 댓글의 대댓글(자식 댓글) 조회합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시물번호", required = true),
            @Parameter(name = "commentNo", description = "부모댓글번호", required = true),
    })
    @GetMapping("/{commentNo}/children")
    public ApiResponse getChildComments(@PathVariable Long postNo, @PathVariable Long commentNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

    /**
     * 댓글 한 개 상세 조회
     */
    @Operation(summary = "댓글 한 개 상세 조회", description = "사용자가 댓글 한 개 상세 조회합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시물번호", required = true),
            @Parameter(name = "commentNo", description = "부모댓글번호", required = true)
    })
    @GetMapping("/{commentNo}/detail")
    public ApiResponse getComment(@PathVariable Long postNo,@PathVariable Long commentNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }

    /**
     * 게시글의 모든 댓글 조회 (부모 + 자식 모두)
     */
    @Operation(summary = "게시글의 모든 댓글 조회", description = "사용자가 게시글의 모든 댓글 조회합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "postNo", description = "게시물번호", required = true),
    })
    @GetMapping("/all")
    public ApiResponse getAllComments(@PathVariable Long postNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }


    @Operation(summary = "게시글의 부모 댓글(댓글) 수정", description = "사용자가 게시글의 부모 댓글 수정합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "commentNo", description = "부모댓글번호", required = true),
            @Parameter(name = "commentNo", description = "대댓글번호", required = true),
            @Parameter(name = "CommentUpdateRequestDto", description = "댓글내용 수정", required = true),
    })
    @PutMapping("/{commentNo}")
    public ApiResponse updateComment(@PathVariable Long postNo,@PathVariable Long commentNo, @RequestBody @Valid CommentRequestDTO.CommentUpdateRequestDto request) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }




    @Operation(summary = "게시글의 대댓글  수정", description = "사용자가 게시글의 대댓글 수정합니다..")

    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "parentNo", description = "부모댓글번호", required = true),
            @Parameter(name = "commentNo", description = "대댓글번호", required = true),
            @Parameter(name = "CommentUpdateRequestDto", description = "댓글내용 수정", required = true),

    })
    @PutMapping("/{parentNo}/children/{commentNo}")
    public ApiResponse updateChildComment(@PathVariable Long postNo, @PathVariable Long parentNo, @PathVariable Long commentNo,
                                          @RequestBody @Valid CommentRequestDTO.CommentUpdateRequestDto request) {
        String resultCode = SuccessStatus._OK.getCode();
        String resultMsg = SuccessStatus._OK.getMessage();

        try {
            // 대댓글 수정 서비스 호출
            // commentService.updateChildComment(postNo, parentNo, commentNo, request); (서비스 호출 부분)
            resultCode = SuccessStatus._OK.getCode();
            resultMsg = "대댓글 수정이 완료되었습니다.";
        } catch (Exception e) {
            resultCode = "ERROR"; // 오류 코드
            resultMsg = "대댓글 수정에 실패했습니다.";
        }

        return new ApiResponse<>(true, resultCode, resultMsg);
    }



    /**
     * 댓글 삭제 (댓글과 대댓글 모두 포함)
     */
    @DeleteMapping("/{commentNo}")
    public ApiResponse deleteComment(@PathVariable Long postNo, @PathVariable Long commentNo) {
        String resultcode = SuccessStatus._OK.getCode();
        String resultmsg = SuccessStatus._OK.getMessage();

        return new ApiResponse<>(true, resultcode, resultmsg);
    }


}