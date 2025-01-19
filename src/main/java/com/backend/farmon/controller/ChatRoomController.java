package com.backend.farmon.controller;

import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.service.ChatRoomService.ChatRoomCommandService;
import com.backend.farmon.validaton.annotation.CheckPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "채팅 페이지", description = "채팅에 관한 API")
@Slf4j
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomCommandService chatRoomCommandService;

    // 전체 채팅 목록 조회
    @Operation(
            summary = "사용자의 전체 채팅 목록 조회 API",
            description = "사용자의 전체 채팅 목록을 조회하는 API이며, 페이징을 포함합니다. " +
                    "유저 아이디, 읽음 여부 필터, 페이지 번호를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAGE4001", description = "페이지 번호는 1 이상이어야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "read", description = "읽음 여부 필터링. 안 읽은 채팅방만 필터링 시에는 false, 이외에는 true 입니다.", example = "false", required = true),
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작입니다.", example = "1", required = true)
    })
    @GetMapping("/rooms/all")
    public ApiResponse<ChatResponse.ChatRoomListDTO> getChatRoomPage (@RequestParam(name = "userId") Long userId,
                                                                      @RequestParam(name = "read") String read,
                                                                      @CheckPage Integer page){
        ChatResponse.ChatRoomListDTO response = ChatResponse.ChatRoomListDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }


    // 이름과 일치하는 채팅 목록 조회
    @Operation(
            summary = "채팅 상대 이름으로 검색하여 일치하는 상대방과의 채팅 목록 조회 API",
            description = "채팅 상대방의 이름을 검색하여 일치하는 상대방과의 채팅 목록을 조회하는 API이며, 페이징을 포함합니다. " +
                    "유저 아이디, 읽음 여부 필터, 검색할 채팅 상대 이름, 페이지 번호를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAGE4001", description = "페이지 번호는 1 이상이어야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "read", description = "읽음 여부 필터링. 안 읽은 채팅방만 필터링 시에는 false, 이외에는 true 입니다.", example = "false", required = true),
            @Parameter(name = "name", description = "검색할 채팅 상대방의 이름입니다.", example = "김팜온", required = true),
            @Parameter(name = "page", description = "페이지 번호, 1부터 시작입니다.", example = "1", required = true)
    })
    @GetMapping("/rooms/name")
    public ApiResponse<ChatResponse.ChatRoomListDTO> getChatRoomPageByExpertName (@RequestParam(name = "userId") Long userId,
                                                                                  @RequestParam(name = "read") String read,
                                                                                  @RequestParam(name = "expertName") String expertName,
                                                                                  @CheckPage Integer page) {
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
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAGE4001", description = "페이지 번호는 1 이상이어야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
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
                                                                                 @CheckPage Integer page) {
        ChatResponse.ChatRoomListDTO response = ChatResponse.ChatRoomListDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }


    // 전문가가 농업인의 견적을 보고 채팅 신청 (채팅방 생성)
    @Operation(
            summary = "전문가가 농업인의 견적을 보고 채팅을 신청하여 채팅방을 생성",
            description = "전문가가 농업인의 견적을 보고 채팅을 신청하여 채팅방을 생성하는 API 입니다. " +
                    "유저 아이디, 견적 아이디를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "EXPERT4001", description = "아이디와 일치하는 전문가가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ESTIMATE4001", description = "견적 아이디와 일치하는 견적이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저(전문가)의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "estimateId", description = "채팅하려는 견적의 아이디", example = "1", required = true),
    })
    @PostMapping("/room")
    public ApiResponse<ChatResponse.ChatRoomCreateDTO> postChatRoom (@RequestParam(name = "userId") Long userId,
                                                                     @RequestParam(name = "estimateId") Long estimateId) {

        ChatResponse.ChatRoomCreateDTO response = chatRoomCommandService.addChatRoom(userId, estimateId);

        return ApiResponse.onSuccess(response);
    }


    // 채팅방 입장
    @Operation(
            summary = "채팅방 아이디와 일치하는 채팅방 입장",
            description = "채팅방 아이디와 일치하는 채팅방 입장에 입장하여 유저의 채팅방 접속 시간을 기록하고, 채팅 대화 상대의 정보를 가져오는 API 입니다. " +
                    "유저 아이디, 채팅방 아이디를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHATROOM4001", description = "채팅방 아이디와 일치하는 채팅방이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "chatRoomId", description = "입장하려는 채팅방의 아이디", example = "1", required = true),
    })
    @PatchMapping("/room")
    public ApiResponse<ChatResponse.ChatRoomEnterDTO> enterChatRoom (@RequestParam(name = "userId") Long userId,
                                                                       @RequestParam(name = "chatRoomId") Long chatRoomId) {
        ChatResponse.ChatRoomEnterDTO response = chatRoomCommandService.updateLastEnterTime(userId, chatRoomId);

        return ApiResponse.onSuccess(response);
    }

    // 채팅 메시지 내역 조회
    @Operation(
            summary = "채팅방 아이디이와 일치하는 채팅방의 대화 내역 조회",
            description = "채팅방 아이디와 일치하는 채팅방의 채팅 대화 내역을 받는 API 입니다. " +
                    "유저 아이디, 채팅방 아이디, 페이지 번호를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200",description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHATROOM4001", description = "채팅방 아이디와 일치하는 채팅방이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "PAGE4001", description = "페이지 번호는 1 이상이어야 합니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "chatRoomId", description = "대화 내역(메시지)을 조회하려는 채팅방의 아이디", example = "1", required = true)
    })
    @GetMapping("/room")
    public ApiResponse<ChatResponse.ChatMessageListDTO> getChatMessageList (@RequestParam(name = "userId") Long userId,
                                                                            @RequestParam(name = "chatRoomId") Long chatRoomId) {
        ChatResponse.ChatMessageListDTO response = chatRoomCommandService.findChatMessageList(userId, chatRoomId);

        return ApiResponse.onSuccess(response);
    }


    // 채팅방 삭제
    @Operation(
            summary = "채팅방 아이디이와 일치하는 채팅방 삭제",
            description = "채팅방 아이디와 일치하는 채팅방을 삭제하는 API 입니다. " +
                    "유저 아이디, 채팅방 아이디를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHATROOM4001", description = "채팅방 아이디와 일치하는 채팅방이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "chatRoomId", description = "삭제하려는 채팅방의 아이디", example = "1", required = true),
    })
    @DeleteMapping("/room")
    public ApiResponse<ChatResponse.ChatRoomDeleteDTO> deleteChatRoom (@RequestParam(name = "userId") Long userId,
                                                                         @RequestParam(name = "chatRoomId") Long chatRoomId) {
        ChatResponse.ChatRoomDeleteDTO response = chatRoomCommandService.removeChatRoom(userId, chatRoomId);

        return ApiResponse.onSuccess(response);
    }


    // 채팅방 - 견적 보기
    @Operation(
            summary = "채팅방에서 견적서 상세 보기",
            description = "채팅방에서 현재 대화중인 견적에 대한 상세를 조회하는 API입니다. " +
                    "유저 아이디, 채팅방 아이디, 견적 아이디를 쿼리 스트링으로 입력해주세요."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON200", description = "OK, 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "USER4001", description = "아이디와 일치하는 사용자가 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "CHATROOM4001", description = "채팅방 아이디와 일치하는 채팅방이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "ESTIMATE4001", description = "견적 아이디와 일치하는 견적이 없습니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "COMMON400", description = "잘못된 요청입니다.", content = @Content(schema = @Schema(implementation = ApiResponse.class))),
    })
    @Parameters({
            @Parameter(name = "userId", description = "로그인한 유저의 아이디(pk)", example = "1", required = true),
            @Parameter(name = "chatRoomId", description = "채팅방의 아이디", example = "1", required = true),
            @Parameter(name = "estimateId", description = "견적서 상세를 보기 위한 견적 아이디", example = "1", required = true),
    })
    @GetMapping("/room/estimate")
    public ApiResponse<ChatResponse.ChatRoomEstimateDTO> getChatRoomEstimate (@RequestParam(name = "userId") Long userId,
                                                                              @RequestParam(name = "chatRoomId") Long chatRoomId,
                                                                              @RequestParam(name = "estimateId") Long estimateId) {
        ChatResponse.ChatRoomEstimateDTO response = ChatResponse.ChatRoomEstimateDTO.builder().build();

        return ApiResponse.onSuccess(response);
    }

}
