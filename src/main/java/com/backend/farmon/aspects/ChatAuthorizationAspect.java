package com.backend.farmon.aspects;

import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.service.ValidationService.ValidationService;
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
public class ChatAuthorizationAspect {
    private final ValidationService validationService;
    private final UserAuthorizationUtil userAuthorizationUtil;

    @Pointcut("execution(* com.backend.farmon.controller.ChatRoomController..*(..))")
    public void aspectChatRoom() {};

    // 채팅방 권한 검증
    // 채팅방과 연관관계가 있는 사용자인지 & 역할이 일치하지는지 검증
    @Around(value = "aspectChatRoom() && args(userId, chatRoomId, ..)", argNames = "joinPoint,userId,chatRoomId")
    public Object validateAuthInChatRoom(ProceedingJoinPoint joinPoint, Long userId, Long chatRoomId) throws Throwable {
        validationService.validateAuthInChatRoom(userId, chatRoomId, userAuthorizationUtil.getCurrentUserRole());
        log.info("AOP를 이용한 채팅방 접근 권한 검증 완료");

        return joinPoint.proceed();
    }
}