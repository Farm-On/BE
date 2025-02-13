package com.backend.farmon.aspects;

import com.backend.farmon.service.ChatRoomService.ChatRoomQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Aspect
@Component
public class AuthorizationAspect {
    private final ChatRoomQueryService chatRoomQueryService;

    @Pointcut("execution(* com.backend.farmon.controller..*(..))")
    public void aspectChatRoom() {}

    // 채팅방에 농입인 or 전문가로 속하는 사용자인지 검증
    @Around(value = "aspectChatRoom() && args(userId, chatRoomId, ..)", argNames = "joinPoint,userId,chatRoomId")
    public Object validateAuthInChatRoom(ProceedingJoinPoint joinPoint, Long userId, Long chatRoomId) throws Throwable {
        chatRoomQueryService.validateAuthInChatRoom(userId, chatRoomId);
        return joinPoint.proceed();
    }
}