package com.backend.farmon.reposiotry.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.QChatRoom;
import com.backend.farmon.domain.QExpert;
import com.backend.farmon.domain.QUser;
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
    QUser farmer = QUser.user;
    QExpert expert = QExpert.expert;

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
}
