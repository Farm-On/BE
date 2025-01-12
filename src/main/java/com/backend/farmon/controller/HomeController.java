package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.dto.home.HomeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1"),
            @Parameter(name = "category", description = " 커뮤니티 카테고리", example = "인기", required = true)
    })
    @GetMapping("")
    public ApiResponse<HomeResponse.HomePageDTO> getChatRoomPage (@RequestParam(name = "userId", required = false) Long userId,
                                                                  @RequestParam(name = "category") String category){
        return null;
    }
}
