package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.dto.chat.ChatResponse;

public interface ChatRoomQueryService {
    // 채팅방 목록 조회
    ChatResponse.ChatRoomListDTO findChatRoom(Long userId, String read, Integer pageNumber);
}
