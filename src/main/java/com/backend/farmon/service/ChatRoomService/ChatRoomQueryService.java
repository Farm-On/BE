package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.dto.chat.ChatResponse;

public interface ChatRoomQueryService {
    // 채팅방 목록 조회
    ChatResponse.ChatRoomListDTO findChatRoom(Long userId, Integer read, Integer pageNumber);

    // 채팅방의 견적 조회
    ChatResponse.ChatRoomEstimateDTO findChatRoomEstimate(Long userId, Long chatRoomId);
}
