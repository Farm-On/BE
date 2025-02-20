package com.backend.farmon.aspects;

import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.service.ValidationService.ValidationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;


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
        // 현재 요청의 HTTP 메서드 확인
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String method = request.getMethod();

        // POST 요청이 아닐 경우에만 검증 수행
        if (!"POST".equalsIgnoreCase(method)) {
            validationService.validateAuthInChatRoom(userId, chatRoomId, userAuthorizationUtil.getCurrentUserRole());
            log.info("AOP를 이용한 채팅방 접근 권한 검증 완료");
        } else {
            log.info("POST 요청(채팅방 생성)이므로 권한 검증을 생략");
        }
        return joinPoint.proceed();
    }
}