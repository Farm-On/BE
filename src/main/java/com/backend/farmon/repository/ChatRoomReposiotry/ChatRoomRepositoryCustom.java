package com.backend.farmon.repository.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {
    // userId와 연관된 채팅방 페이징 조회
    Page<ChatRoom> findChatRoomsByUserIdAndRole(Long userId, String role, Pageable pageable);

    // userId와 연관된 채팅방 중 안 읽음 메시지가 존재하는 채팅방만 페이징 조회
    Page<ChatRoom> findUnReadChatRoomsByUserIdAndRole(Long userId, String role, Pageable pageable);
}
