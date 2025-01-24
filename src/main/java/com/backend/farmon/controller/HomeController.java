package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.dto.post.PostType;
import com.backend.farmon.service.PostService.PostQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "홈 화면", description = "홈 화면에 관한 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/home")
public class HomeController {
    private final PostQueryService postQueryService;

    // 홈 화면 - 커뮤니티 게시글 조회
    @Operation(
            summary = "홈 화면 커뮤니티 게시글 조회 API",
            description = "홈 화면에서 커뮤니티 카테고리에 따른 게시글을 조회합니다. " +
                    "필터링 할 커뮤니티 카테고리 이름을 쿼리 스트링으로 입력해 주세요. "
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "POST_TYPE4001", description = "지원되지 않는 게시판 타입 입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
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
            description = "홈 화면에서 인기 칼럼 게시글 목록을 6개 조회합니다."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @GetMapping("/popular")
    public ApiResponse<HomeResponse.PopularPostListDTO> getHomePopularPosts (){
        HomeResponse.PopularPostListDTO response = postQueryService.findPopularExpertColumnPosts();
        return ApiResponse.onSuccess(response);
    }


    // 최근 검색어 조회
    @Operation(
            summary = "홈 화면 최근 검색어 조회 API",
            description = "홈 화면 검색 창에서 사용자의 최근 검색어들을 조회합니다. 로그인 한 사용자의 경우에는 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk), 로그인 하지 않은 사용자 일 경우 입력하지 않아도 됩니다.", example = "1")
    })
    @GetMapping("/search/recent")
    public ApiResponse<HomeResponse.RecentSearchListDTO> getRecentSearches (@RequestParam(name = "userId", required = false) Long userId){
        HomeResponse.RecentSearchListDTO response = HomeResponse.RecentSearchListDTO.builder().build();;
        return ApiResponse.onSuccess(response);
    }


    // 추천 검색어 조회
    @Operation(
            summary = "홈 화면 추천 검색어 조회 API",
            description = "홈 화면 검색 창에서 추천 검색어들을 조회합니다.  로그인 한 사용자의 경우에는 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk), 로그인 하지 않은 사용자 일 경우 입력하지 않아도 됩니다.", example = "1")
    })
    @GetMapping("/search/recommend")
    public ApiResponse<HomeResponse.RecommendSearchListDTO> getRecommendSearches (@RequestParam(name = "userId", required = false) Long userId){
        HomeResponse.RecommendSearchListDTO response = HomeResponse.RecommendSearchListDTO.builder().build();;
        return ApiResponse.onSuccess(response);
    }


    // 특정 검색어 삭제 API
    @Operation(
            summary = "홈 화면에서 특정 검색어 삭제 API",
            description = "홈 화면에서 특정 검색어를 삭제하는 API 입니다. 유저 아이디와 삭제할 검색어 이름을 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "name", description = "삭제 할 검색어 이름", example = "병충해 관리")
    })
    @DeleteMapping("/search")
    public ApiResponse<HomeResponse.SearchDeleteDTO> deleteSearchName(@RequestParam(name = "userId") Long userId,
                                                                  @RequestParam(name = "name") String searchName){
        HomeResponse.SearchDeleteDTO response = HomeResponse.SearchDeleteDTO.builder().build();;
        return ApiResponse.onSuccess(response);
    }


    // 검색어 전체 삭제 API
    @Operation(
            summary = "홈 화면에서 사용자의 검색어를 전체 삭제 API",
            description = "홈 화면에서 사용자의 검색어를 전체 삭제 API. 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk), 로그인 하지 않은 사용자 일 경우 입력하지 않아도 됩니다.", example = "1")
    })
    @DeleteMapping("/search/all")
    public ApiResponse<HomeResponse.SearchDeleteDTO> deleteAllSearchName(@RequestParam(name = "userId") Long userId) {
        HomeResponse.SearchDeleteDTO response = HomeResponse.SearchDeleteDTO.builder().build();;
        return ApiResponse.onSuccess(response);
    }

    // 홈 화면 검색 - 자동 완성
//    @Operation(
//            summary = "홈 화면 조회 API",
//            description = "홈 화면 로딩에 필요한 정보를 조회합니다. 로그인 한 사용자의 경우에는 유저 아이디를 쿼리 스트링으로 입력해 주세요."
//    )
//    @ApiResponses({
//            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
//    })
//    @Parameters({
//            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk), 로그인 하지 않은 사용자 일 경우 입력하지 않아도 됩니다.", example = "1"),
//    })
//    @GetMapping("/search")
//    public ApiResponse<HomeResponse.HomePageDTO> getChatRoomPage (@RequestParam(name = "userId", required = false) Long userId,
//                                                                  @RequestParam(name = "category") String category){
//        HomeResponse.HomePageDTO response = HomeResponse.HomePageDTO.builder().build();;
//        return ApiResponse.onSuccess(response);
//    }
}
