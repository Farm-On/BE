package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.home.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "홈 화면", description = "홈 화면에 관한 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/home")
public class HomeController {

    // 홈 화면 조회
    @Operation(
            summary = "홈 화면 조회 API",
            description = "홈 화면 로딩에 필요한 정보를 조회합니다. 로그인 한 사용자의 경우에는 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk), 로그인 하지 않은 사용자 일 경우 입력하지 않아도 됩니다.", example = "1"),
            @Parameter(name = "category", description = "커뮤니티 카테고리 이름", example = "인기", required = true)
    })
    @GetMapping("")
    public ApiResponse<HomeResponse.HomePageDTO> getChatRoomPage (@RequestParam(name = "userId", required = false) Long userId,
                                                                  @RequestParam(name = "category") String category){
        return null;
    }

    // 검색어 삭제 API
    @Operation(
            summary = "홈 화면에서 특정 검색어 삭제 API",
            description = "홈 화면에서 특정 검색어를 삭제하는 API 입니다. 유저 아이디와 삭제할 검색어 이름을 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "name", description = "삭제 할 검색어 이름", example = "병충해 관리")
    })
    @DeleteMapping("/search")
    public ApiResponse<HomeResponse.HomePageDTO> deleteSearchName(@RequestParam(name = "userId") Long userId,
                                                                  @RequestParam(name = "name") String searchName){
        return null;
    }

    // 검색어 전체 삭제 API
    @Operation(
            summary = "홈 화면에서 사용자의 검색어를 전체 삭제 API",
            description = "홈 화면에서 사용자의 검색어를 전체 삭제 API. 유저 아이디를 쿼리 스트링으로 입력해 주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1")
    })
    @DeleteMapping("/search/all")
    public ApiResponse<HomeResponse.HomePageDTO> deleteAllSearchName(@RequestParam(name = "userId") Long userId){
        return null;
    }
}
