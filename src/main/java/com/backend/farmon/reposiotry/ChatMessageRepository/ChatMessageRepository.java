package com.backend.farmon.reposiotry.ChatMessageRepository;

import com.backend.farmon.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
    // 채팅방 아이디와 일치하는 가장 최신 메시지 조회
    Optional<ChatMessage> findFirstByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    // 채팅방 아이디와 일치하면서 isRead가 false인 메시지 리스트 조회
    List<ChatMessage> findByChatRoomIdAndIsReadFalse(Long chatRoomId);
}
