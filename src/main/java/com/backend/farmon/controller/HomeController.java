package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.converter.HomeConverter;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.service.PostService.PostQueryService;
import com.backend.farmon.service.SearchService.SearchCommandService;
import com.backend.farmon.service.SearchService.SearchQueryService;
import com.backend.farmon.validaton.annotation.EqualsUserId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Tag(name = "홈 화면", description = "홈 화면에 관한 API")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final PostQueryService postQueryService;
    private final SearchCommandService searchCommandService;
    private final SearchQueryService searchQueryService;
    private CompletableFuture< ResponseEntity<HomeResponse.RecommendSearchListDTO> > recommendSearchListFuture;

    // 홈 화면 - 커뮤니티 게시글 조회
    @Operation(
            summary = "홈 화면 커뮤니티 게시글 조회 API",
            description = "홈 화면에서 커뮤니티 카테고리에 따른 게시글을 조회합니다. " +
                    "필터링 할 커뮤니티 카테고리 이름을 쿼리 스트링으로 입력해 주세요. " +
                    "로그인한 사용자의 경우 발급받은 토큰을 헤더에 입려해주세요. 로그인하지 않은 사용자의 경우에는 토큰을 입력하지 않아도 됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4001", description = "지원되지 않는 게시판 타입 입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "category", description = "커뮤니티 카테고리 이름", example = "POPULAR", required = true)
    })
    @GetMapping("/community")
    public ApiResponse<HomeResponse.PostListDTO> getHomePostsByCategory (@RequestParam(name = "category", defaultValue = "POPULAR") PostType category){
        HomeResponse.PostListDTO response = postQueryService.findHomePostsByCategory(category);
        return ApiResponse.onSuccess(response);
    }


    // 홈 화면 - 인기 칼럼 조회
    @Operation(
            summary = "홈 화면 인기 칼럼 조회 API",
            description = "홈 화면에서 인기 칼럼 게시글 목록을 6개 조회합니다." +
            "로그인한 사용자의 경우 발급받은 토큰을 헤더에 입려해주세요. 로그인하지 않은 사용자의 경우에는 토큰을 입력하지 않아도 됩니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
    })
    @GetMapping("/popular")
    public ApiResponse<HomeResponse.PopularPostListDTO> getHomePopularPosts (){
        HomeResponse.PopularPostListDTO response = postQueryService.findPopularExpertColumnPosts();
        return ApiResponse.onSuccess(response);
    }


    // 홈 화면 검색 - 자동 완성
    @Operation(
            summary = "홈 화면 자동 완성 검색어 목록 조회 API",
            description = "홈 화면에서 검색어 입력 시 사용자가 입력한 검색어와 관련된 작물 카테고리, 이름들을 반환합니다. " +
                    "유저 아이디와 검색어를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORIZATION_4031", description = "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다. (userId 불일치)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "name", description = "검색어", example = "곡"),
    })
    @GetMapping("/search")
    public ApiResponse<HomeResponse.AutoCompleteSearchDTO> getHomeAutoCompleteSearchNameList (@RequestParam(name = "userId") @EqualsUserId Long userId,
                                                                                              @RequestParam(name = "name") String searchName){
        List<String> searchList = searchQueryService.autoSearchNameList(searchName);
        return ApiResponse.onSuccess(HomeConverter.toAutoCompleteSearchDTO(searchList));
    }


    // 홈 화면 검색 - 자동 완성 저장
    @Operation(
            summary = "홈 화면 검색어 저장 API",
            description = "홈 화면 검색어 입력 시 사용자가 입력한 검색어와 관련된 작물 이름들이 반환되었을 때 " +
                    "반환된 작물 이름들 중 사용자가 원하는 작물을 클릭하면 해당 작물 이름을 저장합니다. " +
                    "유저 아이디와 저장할 작물 이름을 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORIZATION_4031", description = "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다. (userId 불일치)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEARCH_4001", description = "검색어가 비어 있습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "name", description = "저장할 작물 이름", example = "채소작물"),
    })
    @PostMapping("/search")
    public ApiResponse<HomeResponse.AutoCompleteSearchPostDTO> postHomeAutoCompleteSearchNameList (@RequestParam(name = "userId") @EqualsUserId Long userId,
                                                                                               @RequestParam(name = "name") String searchName){
        searchCommandService.saveRecentSearchLog(userId, searchName);
        log.info("자동 완성 검색어 저장 - userId: {}, 검색어: {}", userId, searchName);

        return ApiResponse.onSuccess(HomeConverter.toAutoCompleteSearchPostDTO());
    }


    // 최근 검색어 조회
    @Operation(
            summary = "홈 화면 최근 검색어 조회 API",
            description = "홈 화면 검색 창에서 사용자의 최근 검색어들을 조회합니다. 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORIZATION_4031", description = "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다. (userId 불일치)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1")
    })
    @GetMapping("/search/recent")
    public ApiResponse<HomeResponse.RecentSearchListDTO> getRecentSearchNameList (@RequestParam(name = "userId") @EqualsUserId Long userId){
        HomeResponse.RecentSearchListDTO response = searchQueryService.findRecentSearchLogs(userId);
        log.info("최근 검색어 조회 완료 - userId: {}", userId);

        return ApiResponse.onSuccess(response);
    }


    // 특정 검색어 삭제 API
    @Operation(
            summary = "홈 화면에서 최근 검색어에서 특정 검색어 삭제 API",
            description = "홈 화면에서 최근 검색어 목록에서 특정 검색어를 삭제하는 API 입니다. 유저 아이디와 삭제할 검색어 이름을 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORIZATION_4031", description = "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다. (userId 불일치)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "SEARCH_4001", description = "검색어가 비어 있습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "name", description = "삭제 할 검색어 이름", example = "채소작물")
    })
    @DeleteMapping("/search/recent")
    public ApiResponse<HomeResponse.SearchDeleteDTO> deleteSearchName(@RequestParam(name = "userId") @EqualsUserId Long userId,
                                                                      @RequestParam(name = "name") String searchName){
        searchCommandService.deleteRecentSearchLog(userId, searchName);

        return ApiResponse.onSuccess(HomeConverter.toSearchDeleteDTO());
    }


    // 검색어 전체 삭제 API
    @Operation(
            summary = "홈 화면에서 사용자의 검색어를 전체 삭제 API",
            description = "홈 화면에서 사용자의 검색어를 전체 삭제 API. 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORIZATION_4031", description = "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다. (userId 불일치)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1")
    })
    @DeleteMapping("/search/recent/all")
    public ApiResponse<HomeResponse.SearchDeleteDTO> deleteAllSearchName(@RequestParam(name = "userId") @EqualsUserId Long userId) {
        searchCommandService.deleteAllRecentSearchLog(userId);

        return ApiResponse.onSuccess(HomeConverter.toSearchDeleteDTO());
    }

    // 추천 검색어 스케줄링
    // 매월 1일 오전 1시에 스케줄링된 작업, 추천 검색어 리스트 불러오기 실행
    @Scheduled(cron = "0 0 1 1 * *", zone = "Asia/Seoul")
    @Async("customAsyncExecutor")
    public void recommendSearchListSchedule() {
        try {
            log.info("추천 검색어 스케줄링 실행");
            HomeResponse.RecommendSearchListDTO response = searchQueryService.getRecommendSearchNameRank();

            recommendSearchListFuture = CompletableFuture.completedFuture(ResponseEntity.ok().body(response));
        } catch (Exception e) {
            log.warn("추천 검색어 스케줄링 실패: " + e.getMessage());
            recommendSearchListFuture = null;
        }
    }

    // 추천 검색어 조회
    @Operation(
            summary = "홈 화면 추천 검색어 조회 API",
            description = "홈 화면 검색 창에서 추천 검색어들을 조회합니다. 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTHORIZATION_4031", description = "인증된 사용자 정보와 요청된 리소스의 사용자 정보가 다릅니다. (userId 불일치)", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1")
    })
    @GetMapping("/search/recommend")
    public ApiResponse<HomeResponse.RecommendSearchListDTO> getRecommendSearchNameList(@RequestParam(name = "userId") @EqualsUserId Long userId) {
        try {
            // 완료된 스케줄링된 작업이 있는 경우
            if (recommendSearchListFuture != null && recommendSearchListFuture.isDone()) {
                HomeResponse.RecommendSearchListDTO response = recommendSearchListFuture.get().getBody();
                log.info("스케줄링된 추천 검색어 리스트 반환");

                return ApiResponse.onSuccess(response);
            }
        } catch (InterruptedException | ExecutionException e) {
            // 예외 발생 시 에러 응답 반환
            return ApiResponse.onFailure("INTERNAL_SERVER_ERROR", e.getMessage(), null);
        }

        // 스케줄링된 작업이 없거나 실패했을 경우 새로운 추천 검색어 조회 실행
        HomeResponse.RecommendSearchListDTO response = searchQueryService.getRecommendSearchNameRank();
        log.info("스케줄링된 작업이 없어 추천 검색어 리스트 조회 후 반환");

        return ApiResponse.onSuccess(response);
    }

}
