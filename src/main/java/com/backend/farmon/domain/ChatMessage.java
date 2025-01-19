package com.backend.farmon.domain;

import com.backend.farmon.domain.commons.BaseEntity;
import com.backend.farmon.domain.enums.ChatMessageType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
@Entity
public class ChatMessage extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // pk

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ChatMessageType type; // 메시지 타입

    @Column(nullable = false)
    private String content; // 메시지 내용

    @Column(columnDefinition = "TINYINT(1) DEFAULT 0", nullable = false)
    private Boolean isRead; // 메시지 읽음 여부

    @Column(nullable = false)
    private Boolean isMine; // 내가 보낸 메시지인지 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom; // 채팅방과 다대일 양방향
}
