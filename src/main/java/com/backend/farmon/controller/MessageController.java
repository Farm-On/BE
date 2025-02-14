package com.backend.farmon.controller;

import com.backend.farmon.dto.chat.ChatRequest;
import com.backend.farmon.service.ChatMessageService.ChatMessageCommandService;
import com.backend.farmon.validaton.annotation.ExistChatRoom;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;

@Slf4j
@Validated
@Controller
@RequiredArgsConstructor
public class MessageController {
    private final ChatMessageCommandService chatMessageCommandService;
    private final static String CHAT_EXCHANGE_NAME = "chat.exchange";

    private final RabbitTemplate rabbitTemplate;

    // 채팅 메시지 보내기
    // /pub/chat.message.{chatRoomId}
    @MessageMapping("chat.message.{chatRoomId}")
    public void sendChatMessage (@DestinationVariable("chatRoomId") @ExistChatRoom Long chatRoomId,
                                 @Valid ChatRequest.ChatMessageDTO dto) {
        log.info("전송할 메시지 내용: {}", dto);

        // 메시지 저장 로직
        chatMessageCommandService.saveChatMessage(chatRoomId, dto);
        log.info(dto.toString());

        // 구독자들에게 메시지 전달
        // /exchange/chat.exchange/room.{chatRoomId}
        rabbitTemplate.convertAndSend(CHAT_EXCHANGE_NAME, "room." + chatRoomId, dto);
    }

    @RabbitListener(queues = "chat.queue")
    public void consumeChatMessage(ChatRequest.ChatMessageDTO message) {
        log.info("큐에서 받은 메시지: {}", message);
    }

}
