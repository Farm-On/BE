package com.backend.farmon.dto.chat;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class ChatRequest {

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "전송 또는 수신할 채팅 메시지 정보")
    public static class ChatMessageDTO {

        @Schema(description = "보낸 사람 아이디, 현재 로그인한 사용자의 userId와 동일", example = "1")
        Long senderId;

        @Schema(description = "메시지를 보낸 사람의 프로필 이미지")
        String profileImage;

        @Schema(description = "메시지 내용, 이미지일 경우 클라이언트에서 이미지 파일을 Base64로 인코딩하여 전송 필요", example = "안녕하세요. 견적 신청하셨나요?")
        String messageContent;

        @Schema(description = "메시지 타입, " +
                "텍스트 메시지 전송이라면 TEXT, 이미지 전송이라면 IMAGE, 채팅방 퇴장이라면 EXIT", example = "TEXT")
        String messageType;

        @Schema(description = "보낸 사람 타입", example = "농업인")
        String senderType;

        @Schema(description = "보낸 시간", example = "2025-01-10")
        String sendTime;

        @Schema(description = "내가 보낸 메시지인지 여부, 내가 보낸 메시지라면 true, 받은 메시지라면 false", example = "true")
        Boolean isMine;

        @Schema(description = "상대방이 메시지 읽음 여부", example = "false")
        Boolean isOtherRead;
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "전송 또는 수신할 채팅 메시지 정보")
    public static class TestDTO {

        @Schema(description = "보낸 사람 아이디, 현재 로그인한 사용자의 userId와 동일", example = "1")
        Long senderId;
    }
}
