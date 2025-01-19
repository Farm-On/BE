package com.backend.farmon.reposiotry.ChatMessageRepository;

import com.backend.farmon.domain.ChatMessage;
import com.backend.farmon.domain.QChatMessage;
import com.backend.farmon.domain.enums.ChatMessageType;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepositoryImpl implements ChatMessageRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    QChatMessage chatMessage = QChatMessage.chatMessage;

    // 채팅방 아이디와 일치하는 퇴장, 거래 완료가 아닌 채팅 메시지 리스트 조회
    @Override
    public List<ChatMessage> findNonExitCompleteMessagesByChatRoomId(Long chatRoomId) {
        return queryFactory.selectFrom(chatMessage)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.type.notIn(ChatMessageType.EXIT, ChatMessageType.COMPLETE)
                )
                .fetch();
    }

    // 채팅방 아이디와 일치하는 채팅 메시지 중, 상대방이 보낸 메시지 중 안 읽은 메시지를 읽음 처리
    @Transactional
    @Override
    public void updateMessagesToReadByChatRoomId(Long chatRoomId, Long userId) {
        queryFactory.update(chatMessage)
                .set(chatMessage.isRead, true)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.senderId.notIn(userId),
                        chatMessage.isRead.isFalse()
                )
                .execute();
    }

    // 채팅방 아이디와 일치하는 채팅 메시지 중, 안 읽은 퇴장, 거래 완료가 아닌 채팅 메시지 리스트 조회
    @Override
    public List<ChatMessage> findUnreadMessagesByChatRoomId(Long chatRoomId) {
        return queryFactory.selectFrom(chatMessage)
                .where(
                        chatMessage.chatRoom.id.eq(chatRoomId),
                        chatMessage.isRead.isFalse(),
                        chatMessage.type.notIn(ChatMessageType.EXIT, ChatMessageType.COMPLETE)
                )
                .fetch();
    }
}
