package com.backend.farmon.reposiotry.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {
    // userId와 연관된 채팅방 페이징 조회
    Page<ChatRoom> findChatRoomsByUserId(Long userId, Pageable pageable);
}
