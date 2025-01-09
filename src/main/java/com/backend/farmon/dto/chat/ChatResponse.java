package com.backend.farmon.dto.chat;

import lombok.*;

public class ChatResponse {
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RoomListDTO{ // 채팅방 목록
        Long chatRoomId;
    }
}
