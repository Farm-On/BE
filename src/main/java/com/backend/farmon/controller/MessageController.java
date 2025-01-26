package com.backend.farmon.controller;

import com.backend.farmon.dto.chat.ChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 채팅 메시지 보내기
    // /send/chat/message/{chatRoomId}
    @MessageMapping(value="/chat/message/{chatRoomId}")
    public void sendChatMessage (@DestinationVariable("chatRoomId") Long chatRoomId,
                                 ChatRequest.ChatMessageDTO dto) {
        log.info("전송할 메시지 내용: {}", dto);

        switch (dto.getMessageType()){
            case "ENTER": // 입장 - 접속시간 변경
            case "TEXT": // 텍스트 로직
            case "IMAGE": // 이미지 로직
            case "COMPLETE": // 컨설팅 완료 -> 견적에서 estimateId 도 업데이트 되도록 수정
            case "EXIT": // 퇴장 - 접속시간 변경
        }

        // 메시지 저장 로직

        // 구독자들에게 메시지 전달
        // /receive/chat/message/{chatRoomId}
        simpMessagingTemplate.convertAndSend("/receive/chat/message/"+chatRoomId, dto);
    }
}
