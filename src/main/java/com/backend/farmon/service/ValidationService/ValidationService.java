package com.backend.farmon.service.ValidationService;

import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.User;

public interface ValidationService {
    User validateUser(Long userId);

    ChatRoom validateChatRoom(Long chatRoomId);

    Estimate validateEstimate(Long estimateId);

    // User 존재 여부 검증
    Boolean existsUserById(Long userId);

    // 채팅방 존재 여부 검증
    Boolean existsChatRoomById(Long chatRoomId);

    // 채팅방 접근 권한 검증
    void validateAuthInChatRoom(Long userId, Long chatRoomId, String role);
}
