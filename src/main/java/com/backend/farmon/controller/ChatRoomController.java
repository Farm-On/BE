package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.chat.ChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    // 전체 채팅 목록 조회
    @GetMapping("/room/all")
    @Operation(summary = "사용자의 전체 채팅 목록 조회 API", description = "사용자의 전체 채팅 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 포함 해주세요",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH005", description = "access 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "가게의 아이디, query parameter 입니다."),
            @Parameter(name = "page", description = "페이지 번호, query parameter 입니다.")
    })
    ApiResponse<ChatResponse.RoomListDTO> getChatRoomPage (@RequestParam(name = "userId") Long userId,
                                                           @RequestParam(name = "page")  Integer page){
        ChatResponse.RoomListDTO response = ChatResponse.RoomListDTO.builder()
                                                .chatRoomId(1L)
                                                .build();

        return ApiResponse.onSuccess(response);
    }

    // 전문가 이름이 일치하는 전체 채팅 목록 조회
    @GetMapping("/room/expert")
    @Operation(summary = "전문가 이름으로 검색한 사용자의 전체 채팅 목록 조회 API", description = "전문가 이름을 검색하여 일치하는 전체 채팅 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH003", description = "access 토큰을 포함 해주세요",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH004", description = "access 토큰 만료",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "AUTH005", description = "access 토큰 모양이 이상함",content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "가게의 아이디, query parameter 입니다."),
            @Parameter(name = "page", description = "페이지 번호, query parameter 입니다."),
            @Parameter(name = "expertName", description = "검색할 전문가 이름입니다."),
    })
    ApiResponse<ChatResponse.RoomListDTO> getChatRoomPageByExpertName (@RequestParam(name = "userId") Long userId,
                                                                       @RequestParam(name = "expertName") String expertName,
                                                                       @RequestParam(name = "page")  Integer page) {
        ChatResponse.RoomListDTO response = ChatResponse.RoomListDTO.builder()
                .chatRoomId(1L)
                .build();

        return ApiResponse.onSuccess(response);
    }
}
