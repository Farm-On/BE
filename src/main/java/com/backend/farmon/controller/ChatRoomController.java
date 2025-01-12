package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.chat.ChatResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채팅 페이지", description = "채팅에 관한 API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatRoomController {

    // 전체 채팅 목록 조회
    @Operation(
            summary = "사용자의 전체 채팅 목록 조회 API",
            description = "사용자의 전체 채팅 목록을 조회하는 API이며, 페이징을 포함합니다. " +
                    "유저 아이디, 읽음 여부 필터, 페이지 번호를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "read", description = "읽음 여부 필터링. 안 읽은 채팅방만 필터링 시에는 false, 이외에는 true 입니다.", example = "false", required = true),
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작입니다.", example = "1", required = true)
    })
    @GetMapping("/rooms/all")
    public ApiResponse<ChatResponse.ChatRoomListDTO> getChatRoomPage (@RequestParam(name = "userId") Long userId,
                                                                      @RequestParam(name = "read") String read,
                                                                      @RequestParam(name = "page")  Integer page){
        ChatResponse.ChatRoomListDTO response = ChatResponse.ChatRoomListDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }


    // 전문가 이름과 일치하는 채팅 목록 조회
    @Operation(
            summary = "전문가 이름으로 검색하여 일치하는 전문가와의 채팅 목록 조회 API",
            description = "전문가 이름을 검색하여 일치하는 채팅 목록을 조회하는 API이며, 페이징을 포함합니다. " +
                    "유저 아이디, 읽음 여부 필터, 검색할 전문가 이름, 페이지 번호를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "read", description = "읽음 여부 필터링. 안 읽은 채팅방만 필터링 시에는 false, 이외에는 true 입니다.", example = "false", required = true),
            @Parameter(name = "expertName", description = "검색할 전문가 이름입니다.", example = "김팜온", required = true),
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작입니다.", example = "1", required = true)
    })
    @GetMapping("/rooms/expert")
    public ApiResponse<ChatResponse.ChatRoomListDTO> getChatRoomPageByExpertName (@RequestParam(name = "userId") Long userId,
                                                                                  @RequestParam(name = "read") String read,
                                                                                  @RequestParam(name = "expertName") String expertName,
                                                                                  @RequestParam(name = "page")  Integer page) {
        ChatResponse.ChatRoomListDTO response = ChatResponse.ChatRoomListDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }


    // 작물 이름으로 검색하여 일치하는 전문가와의 채팅 목록 조회
    @Operation(
            summary = "작물 이름으로 검색하여 일치하는 전문가와의 채팅 목록 조회",
            description = "작물 이름으로 검색하여 일치하는 해당 분야의 전문가와의 채팅 목록을 조회하는 API이며 페이징을 포함합니다. " +
                    "유저 아이디, 읽음 여부 필터, 검색할 전문가의 작물 이름, 페이지 번호를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "read", description = "읽음 여부 필터링. 안 읽은 채팅방만 필터링 시에는 false, 이외에는 true 입니다.", example = "1", required = true),
            @Parameter(name = "cropsName", description = "검색할 전문가의 작물 이름(쌀, 곡물 등)입니다.", example = "쌀", required = true),
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작입니다.", example = "1", required = true)
    })
    @GetMapping("/rooms/crops")
    public ApiResponse<ChatResponse.ChatRoomListDTO> getChatRoomPageByCropsName (@RequestParam(name = "userId") Long userId,
                                                                                 @RequestParam(name = "read") String read,
                                                                                 @RequestParam(name = "cropsName") String cropsName,
                                                                                 @RequestParam(name = "page")  Integer page) {
        ChatResponse.ChatRoomListDTO response = ChatResponse.ChatRoomListDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }


    // 채팅방 입장
    @Operation(
            summary = "채팅방 아이디이와 일치하는 채팅방 입장",
            description = "채팅방 아이디와 일치하는 채팅방 입장에 채팅 대화 내역을 받는 API 입니다. " +
                    "유저 아이디, 채팅방 아이디를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "chatRoomId", description = "입장하려는 채팅방의 아이디", example = "1", required = true),
    })
    @GetMapping("/room")
    public ApiResponse<ChatResponse.ChatMessageListDTO> enterChatRoom (@RequestParam(name = "userId") Long userId,
                                                                       @RequestParam(name = "chatRoomId") Long chatRoomId) {
        ChatResponse.ChatMessageListDTO response = ChatResponse.ChatMessageListDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }


    // 채팅방 삭제
    @Operation(
            summary = "채팅방 아이디이와 일치하는 채팅방 삭제",
            description = "채팅방 아이디와 일치하는 채팅방을 삭제하는 API 입니다. " +
                    "유저 아이디, 채팅방 아이디를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공")
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "chatRoomId", description = "삭제하려는 채팅방의 아이디", example = "1", required = true),
    })
    @DeleteMapping("/room")
    public ApiResponse<ChatResponse.ChatRoomDeleteDTO> deleteChatRoom (@RequestParam(name = "userId") Long userId,
                                                                       @RequestParam(name = "chatRoomId") Long chatRoomId) {
        ChatResponse.ChatRoomDeleteDTO response = ChatResponse.ChatRoomDeleteDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }

}
