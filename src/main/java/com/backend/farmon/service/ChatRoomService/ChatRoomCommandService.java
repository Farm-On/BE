package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.dto.chat.ChatResponse;

public interface ChatRoomCommandService {

    // 채팅방 생성
    ChatResponse.ChatRoomCreateDTO addChatRoom(Long userId, Long estimateId);

    // 채팅방 삭제
    ChatResponse.ChatRoomDeleteDTO removeChatRoom(Long userId, Long chatRoomId);
}
