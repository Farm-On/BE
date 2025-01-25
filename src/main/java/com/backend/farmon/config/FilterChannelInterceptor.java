package com.backend.farmon.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 99) // 우선순위 올리기
public class FilterChannelInterceptor implements ChannelInterceptor {

    // 메시지가 채널로 전송되기 전 호출되는 메서드
    // 메시지 전송 전에 인증 토큰을 검증하거나, 메시지 내용을 필터링 가능
    @Override
    @Transactional
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        log.info("full message:" + message);

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        System.out.println("auth:" + headerAccessor.getNativeHeader("Authorization"));

        // stomp 연결 시
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())){
        }

        return message;
    }
}