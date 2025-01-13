package com.backend.farmon.controller;

import com.backend.farmon.dto.like.LikeRequestDTO;
import com.backend.farmon.dto.like.LikeResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.backend.farmon.apiPayload.ApiResponse;

import static com.backend.farmon.apiPayload.ApiResponse.onSuccess;
@Tag(name = "좋아요 페이지", description = "좋아요 기능에 관한 API")
@Controller
@RequestMapping("posts/{boardType}/list/{postNo}/likes")
public class LikeController {


    /**
     * 좋아요 추가
     */
    @Operation(summary = "좋아요 추가", description = "사용자가 특정 게시글에 좋아요를 추가합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @PostMapping
    public ApiResponse<LikeResponseDTO> addLike(
            @PathVariable("boardType") String boardType,
            @PathVariable("postNo") Long postNo,
            @RequestBody LikeRequestDTO likeRequestDTO) {
        Long userId = likeRequestDTO.getUserId();
        int cnt=1;//실제 서비스 로직
        LikeResponseDTO response = LikeResponseDTO.builder()
                .targetId(postNo)
                .userId(userId)
                .likeCount(cnt)
                .build();

        return ApiResponse.onSuccess(response);
    }

    /**
     * 좋아요 삭제
     */

    @Operation(summary = "좋아요 삭제", description = "사용자가 특정 게시글의 좋아요를 취소합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @DeleteMapping
    public ApiResponse<Integer> removeLike(
            @PathVariable("boardType") String boardType,
            @PathVariable("postNo") Long postId,
            @RequestBody LikeRequestDTO likeRequestDTO) {

        Long userId = likeRequestDTO.getUserId();
        int updatedLikeCount = 1;

        return ApiResponse.onSuccess(updatedLikeCount);
    }

    /**
     * 좋아요 개수 조회
     */

    @Operation(summary = "좋아요 개수 조회", description = "특정 게시글의 좋아요 개수를 조회합니다.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OK, 성공")
    })
    @GetMapping("/count")
    public ApiResponse<Integer> getLikeCount(
            @PathVariable("boardType") String boardType,
            @PathVariable("postNo") Long postId) {
        int likeCount = 1;
        return ApiResponse.onSuccess(likeCount);
    }
}
