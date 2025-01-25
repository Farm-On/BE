package com.backend.farmon.repository.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.QChatMessage;
import com.backend.farmon.domain.QChatRoom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QChatRoom chatRoom = QChatRoom.chatRoom;
    QChatMessage chatMessage = QChatMessage.chatMessage;

    // userId와 연관된 채팅방 페이징 조회
    @Override
    public Page<ChatRoom> findChatRoomsByUserIdAndRole(Long userId, String role, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(chatRoom.farmer.id.eq(userId).or(chatRoom.expert.user.id.eq(userId)));

        // role에 따른 조건 추가
        if ("FARMER".equalsIgnoreCase(role)) {
            builder.and(chatRoom.farmer.id.eq(userId));
        } else if ("EXPERT".equalsIgnoreCase(role)) {
            builder.and(chatRoom.expert.user.id.eq(userId));
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        // 데이터 조회 쿼리
        QueryResults<ChatRoom> results = queryFactory
                .selectFrom(chatRoom)
                .where(builder)
                .orderBy(chatRoom.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        // Page 객체 반환
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }

    // userId와 연관된 채팅방 중 안 읽음 메시지가 존재하는 채팅방만 페이징 조회
    @Override
    public Page<ChatRoom> findUnReadChatRoomsByUserIdAndRole(Long userId, String role, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        // role에 따른 조건 추가
        if ("FARMER".equalsIgnoreCase(role)) {
            builder.and(chatRoom.farmer.id.eq(userId));
        } else if ("EXPERT".equalsIgnoreCase(role)) {
            builder.and(chatRoom.expert.user.id.eq(userId));
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        builder.and(chatMessage.isRead.isFalse());

        // 데이터 조회 쿼리
        QueryResults<ChatRoom> results = queryFactory
                .select(chatRoom)
                .from(chatRoom)
                .join(chatMessage).on(chatMessage.chatRoom.id.eq(chatRoom.id))
                .where(builder)
                .orderBy(chatRoom.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        // Page 객체 반환
        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
