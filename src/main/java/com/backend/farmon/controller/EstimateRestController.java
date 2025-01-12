package com.backend.farmon.controller;

import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.backend.farmon.apiPayload.ApiResponse;
@Tag(name = "농사 견적서", description = "농사 견적서 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estimate")
public class EstimateRestController {
    /**
     * 1) 농사견적서 작성
     */
    @Operation(
            summary = "농사 견적서 생성 API",
            description = "새로운 농사 견적서를 작성하는 API입니다. " +
                    "작성자 ID와 견적서 내용(카테고리, 견적, 상세 내용 등)을 RequestBody 등에 담아 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @PostMapping
    public ApiResponse<EstimateResponseDTO.CreateDTO> createEstimate(
            @RequestBody EstimateRequestDTO.CreateDTO request
    ) {
        // 실제 로직(저장)은 생략
        // 예시로 작성된 Dto 를 그대로 반환
        // 여기서 response에는 DB 저장 후 생성된 estimateId 등을 담았다고 가정

        EstimateResponseDTO.CreateDTO response = EstimateResponseDTO.CreateDTO.builder()
                .estimateId(1L)
                .userId(request.getUserId())
                .build();

        return ApiResponse.onSuccess(response);
    }

    /**
     * 2) 내가 작성한 농사견적서 리스트 불러오기
     */
    @Operation(
            summary = "내가 작성한 농사 견적서 리스트 조회 API",
            description = "로그인된 사용자(작성자)의 모든 농사 견적서를 페이징으로 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "page", description = "페이지 번호 (1부터 시작)", example = "1", required = true)
    })
    @GetMapping("/my")
    public ApiResponse<EstimateResponseDTO.ListDTO> etMyEstimates(
            @RequestParam(name = "userId") Long userId,
            @RequestParam(name = "page") Integer page
    ){
        // 실제 조회 로직 생략, 페이징된 리스트라고 가정
        EstimateResponseDTO.ListDTO response = EstimateResponseDTO.ListDTO.builder()
                .totalCount(2L)
                .currentPage(page)
                .estimates(/* 실제라면 목록을 넣음 */ null)
                .build();
        return ApiResponse.onSuccess(response);
    }

    /**
     * 3) 모든 농업인이 올린 농사 견적서 농작물 카테고리 별로 불러오기
     */
    @Operation(
            summary = "카테고리별 농사 견적서 목록 조회 API",
            description = "모든 사용자가 올린 농사 견적서 중, 지정된 카테고리명에 해당하는 견적서를 조회합니다. " +
                    "카테고리명과 페이지번호를 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "category", description = "농작물 카테고리 명", example = "곡물", required = true),
            @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1", required = true)
    })
    @GetMapping("/category")
    public ApiResponse<EstimateResponseDTO.ListDTO> getEstimateByCategory(
            @RequestParam(name = "categoryId") String category,
            @RequestParam(name = "page") Integer page
    ){
        EstimateResponseDTO.ListDTO response = EstimateResponseDTO.ListDTO.builder()
                .totalCount(3L)
                .currentPage(page)
                .estimates(/* 실제라면 목록을 넣음 */ null)
                .build();

        return ApiResponse.onSuccess(response);
    }

    /**
     * 4) 견적서 읽기 (선택된 견적서 상세 조회)
     */

    @Operation(
            summary = "견적서 상세 조회 API",
            description = "견적서 id와 일치하는 견적서를 상세조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "estimateId", description = "조회하려는 견적서의 id(pk)", example = "100", required = true)
    })
    @GetMapping("/{estimateId}")
    public ApiResponse<EstimateResponseDTO.DetailDTO> getEstimateDetail(
            @PathVariable Long estimateId
    ) {
        // 실제 조회 로직 대신 예시
        EstimateResponseDTO.DetailDTO response = EstimateResponseDTO.DetailDTO.builder()
                .estimateId(estimateId)
                .cropName("옥수수")
                .userName("이지수")
                .category("곡물")
                .address("경기도")
                .budget("1000~2000")
                .body("")
                .build();

        return ApiResponse.onSuccess(response);
    }


    /**
     * 5) 견적서 수정
     */
    @Operation(
            summary = "견적서 수정 api",
            description = "견적서 id에 해당하는 내용을 수정합니다." +
                    "수정할 내용(카테고리, 내용, 주소 등)을 RequestBody로 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "estimatedId", description = "수정할 견적서의 ID(pk)", example = "100", required = true)
    })
    @PutMapping("/{estimateId}")
    public ApiResponse<EstimateResponseDTO.UpdateDTO> updateEstimate(
            @PathVariable Long estimateId,
            @RequestBody EstimateRequestDTO.CreateDTO request
    ){
        // 실제 로직 생략, 예시로 수정 내용 반환
        EstimateResponseDTO.UpdateDTO response = EstimateResponseDTO.UpdateDTO.builder()
                .estimateId(1l)
                .userId(request.getUserId())
                .build();
        return ApiResponse.onSuccess(response);
    }

    /**
     * 6) 견적서 삭제
     */
    @Operation(
            summary = "견적서 삭제 API",
            description = "견적서 ID와 일치하는 견적서를 삭제합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "estimateId", description = "삭제할 견적서의 ID(pk)", example = "100", required = true)
    })
    @DeleteMapping("/{estimateId}")
    public ApiResponse<EstimateResponseDTO.DeleteDTO> deleteEstimate(
            @PathVariable Long estimateId
    ) {
        // 실제 삭제 로직 생략, 예시로 삭제 결과 DTO 반환
        EstimateResponseDTO.DeleteDTO response = EstimateResponseDTO.DeleteDTO.builder()
                .estimateId(estimateId)
                .deleted(true)
                .build();

        return ApiResponse.onSuccess(response);

    }
}
