package com.backend.farmon.reposiotry.ChatRoomReposiotry;

import com.backend.farmon.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChatMessage chatMessage = QChatMessage.chatMessage;

    // userId와 연관된 채팅방 페이징 조회
    @Override
    public Page<ChatRoom> findChatRoomsByUserId(Long userId, Pageable pageable) {

        List<ChatRoom> content = queryFactory.selectFrom(chatRoom)
                .where(chatRoom.farmer.id.eq(userId)
                        .or(chatRoom.expert.user.id.eq(userId)))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체 반환 (total은 조회된 데이터의 크기로 설정)
        return new PageImpl<>(content, pageable, content.size());
    }

    // userId와 연관된 채팅방 중 안 읽음 메시지가 존재하는 채팅방만 페이징 조회
    @Override
    public Page<ChatRoom> findUnReadChatRoomsByUserId(Long userId, Pageable pageable){
        List<ChatRoom> content = queryFactory.select(chatRoom)
                .from(chatRoom)
                .join(chatMessage).on(chatMessage.chatRoom.id.eq(chatRoom.id))
                .where(
                        chatRoom.farmer.id.eq(userId).or(chatRoom.expert.user.id.eq(userId)), // 사용자 ID 조건
                        chatMessage.isRead.isFalse() // 읽지 않은 메시지 조건
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // Page 객체 반환 (total은 조회된 데이터의 크기로 설정)
        return new PageImpl<>(content, pageable, content.size());
    }

}
