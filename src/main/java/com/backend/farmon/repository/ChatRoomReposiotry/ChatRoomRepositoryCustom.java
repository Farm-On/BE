package com.backend.farmon.repository.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatRoomRepositoryCustom {
    // userId와 연관된 검색어와 일치하는 채팅방 페이징 조회
    Page<ChatRoom> findChatRoomsByUserIdAndRoleAndSearch(Long userId, String role, String searchName, Pageable pageable);

    // userId와 연관된 검색어와 일치하는 채팅방 중 안 읽음 메시지가 존재하는 채팅방만 페이징 조회
    Page<ChatRoom> findUnReadChatRoomsByUserIdAndRoleAndSearch(Long userId, String role, String searchName, Pageable pageable);

    // 채팅방에서 농업인 여부
    Boolean isFarmerInChatRoom(Long userId, Long chatRoomId);

    // 사용자가 로그인한 역할(role)로 채팅방에 속해 있는지와 해당 채팅방에 접근할 권한이 있는 사용자인지 검증
    String checkUserRoleInChatRoom(Long userId, Long chatRoomId, String role);
}
