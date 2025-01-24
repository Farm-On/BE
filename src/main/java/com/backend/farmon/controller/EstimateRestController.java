package com.backend.farmon.controller;

import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.service.EstimateService.EstimateCommandService;
import com.backend.farmon.service.EstimateService.EstimateQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import com.backend.farmon.apiPayload.ApiResponse;
@Tag(name = "농사 견적서", description = "농사 견적서 관련 API")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/estimate")
public class EstimateRestController {

    private final EstimateCommandService estimateCommandService;
    private final EstimateQueryService estimateQueryService;
    /**
     * (1) 견적서 생성 (농업인 전용)
     */
    @Operation(
            summary = "농사 견적서 생성 API",
            description = "새로운 농사 견적서를 작성하는 API 입니다. " +
                    "작성자 ID와 견적서 내용(카테고리, 견적, 상세 내용 등)을 RequestBody 등에 담아 보내주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @PostMapping
    public ApiResponse<EstimateResponseDTO.CreateDTO> createEstimate(
            @RequestBody EstimateRequestDTO.CreateDTO request
    ) {
        // 실제 로직(저장)은 생략
        // 예시로 작성된 Dto 를 그대로 반환
        // 여기서 response 에는 DB 저장 후 생성된 estimateId 등을 담았다고 가정

        EstimateResponseDTO.CreateDTO response =  estimateCommandService.createEstimate(request);

        return ApiResponse.onSuccess(response);
    }

    /**
     * 2) 견적서 읽기 (선택된 견적서 상세 조회)
     */

    @Operation(
            summary = "견적서 상세 조회 API",
            description = "견적서 id와 일치하는 견적서를 상세조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "estimateId", description = "조회하려는 견적서의 id(pk)", example = "100", required = true)
    })
    @GetMapping("/{estimateId}")
    public ApiResponse<EstimateResponseDTO.DetailDTO> getEstimateDetail(
            @PathVariable Long estimateId
    ) {
        // 실제 조회 로직 대신 예시
        EstimateResponseDTO.DetailDTO response = estimateQueryService.getEstimateDetail(estimateId);

        return ApiResponse.onSuccess(response);
    }


    /**
     * 3) 견적서 수정
     */
    @Operation(
            summary = "견적서 수정 API",
            description = "견적서 id에 해당하는 내용을 수정합니다." +
                    "수정할 내용(카테고리, 내용, 주소 등)을 RequestBody 로 보내주세요."
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
            @RequestBody EstimateRequestDTO.UpdateDTO request
    ){
        // 실제 로직 생략, 예시로 수정 내용 반환
        EstimateResponseDTO.UpdateDTO response = estimateCommandService.updateEstimate(estimateId, request);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 4) 견적서 삭제
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
        EstimateResponseDTO.DeleteDTO response = estimateCommandService.deleteEstimate(estimateId);

        return ApiResponse.onSuccess(response);

    }

    /**
     * (5-1) 전문가 - 작물 ID 에 해당되는 견적서만 불러오기 (최신순 10개씩 페이징)
     *     "전문가테이블-작물 ID"를 기준으로 조회
     *     예: 전문가가 '옥수수(작물 ID=3)'를 담당한다면,
     *         견적서 중 cropId가 3인 것만 최신순 10개씩
     */
    @Operation(
            summary = "전문가용 견적서 조회 API - 전문가의 작물 ID 기반(10개 페이징 최신순)",
            description = "전문가(Expert)가 전문하는 작물 ID 기반으로" +
            "견적서를 최신순으로 10개 페이징하여 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "expertId", description = "전문가 ID", example = "10", required = true),
            @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1", required = true)
    })
    @GetMapping("expert/{expertId}/by-crop")
    public ApiResponse<EstimateResponseDTO.ListDTO> getEstimatesByExpertCropId(
            @PathVariable Long expertId,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ){
        // 서비스 로직 예시:
        //   1) expertId로 전문가 정보 조회 -> List<Long> cropIds
        //   2) cropIds에 속하는 estimate만 조회 -> 최신순 정렬 -> 10개씩 페이징
        EstimateResponseDTO.ListDTO response = estimateQueryService.getEstimateListByExpertCropId(expertId, page);

        return ApiResponse.onSuccess(response);
    }

    /**
     * (5-2) 견적서의 작물 카테고리별로 불러오는 API (최신순 10개씩 페이징)
     *     10개 페이징/최신순
     */
    @Operation(
            summary = "전문가용 작물 카테고리별 견적서 조회 API(10개 페이징, 최신순)",
            description = "지정된 작물 카테고리(cropCategory)에 해당하는 견적서를 " +
                    "최신순으로 정렬하여 10개씩 페이징으로 제공합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "cropCategory", description = "작물 카테고리 이름", example = "GRAIN", required = true),
            @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1", required = true)
    })
    @GetMapping("/expert/crop-category")
    public ApiResponse<EstimateResponseDTO.ListDTO> getEstimatesByCropCategory(
            @RequestParam(name = "cropCategory") String cropCategory,
            @RequestParam(name = "page") Integer page
    ) {
        // 실제 로직 예시:
        //   1) cropCategory로 해당되는 estimate만 조회
        //   2) 최신순으로 정렬, 10개씩 페이징
        EstimateResponseDTO.ListDTO response = estimateQueryService.getEstimateListByCropCategory(cropCategory, page);

        return ApiResponse.onSuccess(response);
    }

    /**
     * (5-3) 견적서의 세부 작물별로 불러오는 API (최신순 10개씩 페이징)
     *     10개 페이징/최신순
     */
    @Operation(
            summary = "전문가용 세부 작물별 견적서 조회 API(10개 페이징, 최신순)",
            description = "지정된 작물(cropId)에 해당하는 견적서를 " +
                    "최신순으로 정렬하여 10개씩 페이징으로 제공합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "cropId", description = "작물 id", example = "10", required = true),
            @Parameter(name = "page", description = "페이지 번호(1부터 시작)", example = "1", required = true)
    })
    @GetMapping("/expert/crop-id")
    public ApiResponse<EstimateResponseDTO.ListDTO> getEstimatesByCropId(
            @RequestParam(name = "cropId") Long cropId,
            @RequestParam(name = "page") Integer page
    ) {
        // 실제 로직 예시:
        //   1) cropCategory 로 해당되는 estimat e만 조회
        //   2) 최신순으로 정렬, 10개씩 페이징
        EstimateResponseDTO.ListDTO response = estimateQueryService.getEstimateListByCropId(cropId, page);

        return ApiResponse.onSuccess(response);
    }

    /**
     * 6) 전문가-내 견적 API (최신순 9개씩 페이징)
     *     9개 페이징/최신순
     */
    @Operation(
            summary = "전문가용 내 견적 모두 보기 API",
            description = "전문가(expertId)에 해당하는 견적서 목록을 모두 조회합니다." +
                    "최신순으로 9개씩 페이징"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "expertId", description = "전문가 ID", required = true),
            @Parameter(name = "page", description = "페이지 번호", required = true)
    })
    @GetMapping("/expert/{expertId}/all")
    public ApiResponse<EstimateResponseDTO.ListDTO> getInProgressEstimatesForExpert(
            @PathVariable Long expertId,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        // 실제 로직 예시:
        //   1) expertId로 배정된 Estimate 중  status 가 0인 것만 조회
        //   2) 최신순 or 원하는 기준으로 페이징

        EstimateResponseDTO.ListDTO response = estimateQueryService.getAllEstimateListByExpertId(expertId, page);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 7) 전문가-완료한 견적 API (최신순 9개씩 페이징)
     *     9개 페이징/최신순
     */
    @Operation(
            summary = "전문가용 완료된 견적 API",
            description = "전문가(expertId)가 완료된(1) 견적서 목록을 조회합니다." +
                    "최신순으로 9개씩 페이징"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "expertId", description = "전문가 ID", required = true),
            @Parameter(name = "page", description = "페이지 번호", required = true)
    })
    @GetMapping("/expert/{expertId}/is-complete")
    public ApiResponse<EstimateResponseDTO.ListDTO> getIsCompleteEstimatesForExpert(
            @PathVariable Long expertId,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        // 실제 로직 예시:
        //   1) expertId로 배정된 Estimate 중 status 가 1 인 것만 조회
        //   2) 최신순 or 원하는 기준으로 페이징

        EstimateResponseDTO.ListDTO response = estimateQueryService.getCompletedEstimateListByExpertId(expertId, page);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 6) 농업인-내 견적 모두 보기 API (최신순 30개씩 페이징)
     *     30개 페이징/최신순
     */
    @Operation(
            summary = "농업인용 내 견적 모두 보기 API",
            description = "농업인(userId)에 해당하는 견적서 목록을 모두 조회합니다." +
                    "최신순으로 30개씩 페이징"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "농업인 ID", required = true),
            @Parameter(name = "page", description = "페이지 번호", required = true)
    })
    @GetMapping("/user/{userId}/all")
    public ApiResponse<EstimateResponseDTO.ListDTO> getInProgressEstimatesForUser(
            @PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        // 실제 로직 예시:
        //   1) userId 배정된 Estimate 중 status 1 인 것만 조회
        //   2) 최신순 or 원하는 기준으로 페이징

        EstimateResponseDTO.ListDTO response = estimateQueryService.getAllEstimateListByUserId(userId, page);
        return ApiResponse.onSuccess(response);
    }

    /**
     * 7) 농업인-완료한 견적 API (최신순 30개씩 페이징)
     *     30개 페이징/최신순
     */
    @Operation(
            summary = "농업인용 완료된 견적 API",
            description = "농업인(userId)가 완료된(1) 견적서 목록을 조회합니다." +
                    "최신순으로 30개씩 페이징"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "농업인 ID", required = true),
            @Parameter(name = "page", description = "페이지 번호", required = true)
    })
    @GetMapping("/user/{userId}/is-complete")
    public ApiResponse<EstimateResponseDTO.ListDTO> getIsCompleteEstimatesForUser(
            @PathVariable Long userId,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ) {
        // 실제 로직 예시:
        //   1) userId로 배정된 Estimate 중 status 1 인 것만 조회
        //   2) 최신순 or 원하는 기준으로 페이징

        EstimateResponseDTO.ListDTO response = estimateQueryService.getCompletedEstimateListByUserId(userId, page);
        return ApiResponse.onSuccess(response);
    }

    /**
     * (8) 농업인 - 내 등록견적 중 최신순 5개
     */
    @Operation(
            summary = "농업인용 견적서 5개 조회",
            description = "농업인(userId)이 등록한 견적서 중 " +
                    "최신순으로 최대 5개를 반환합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.
                    ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "농업인 ID", required = true)
    })
    @GetMapping("/user/{userId}/recent5")
    public ApiResponse<EstimateResponseDTO.ListDTO> getTop5EstimatesForFarmer(
            @PathVariable Long userId
    ) {
        // 실제 로직 예시:
        //   1) userId로 등록된 견적서 중
        //   2) 최신순 정렬 후 상위 5개만
        EstimateResponseDTO.ListDTO response = estimateQueryService.getRecent5EstimateListByUserId(userId);
        return ApiResponse.onSuccess(response);
    }

    /**
     * (9) 필터링(검색/조건) 예시 - 마지막에
     *  - 예: 지역, 예산 범위, 견적 카테고리 를 query param으로 받아서 검색
     *  - 실제 구현에서는 여러 파라미터들을 받아서 동적 쿼리를 구성해야 함.
     */






}
