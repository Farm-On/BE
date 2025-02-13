package com.backend.farmon.validaton.annotation;

import com.backend.farmon.validaton.validator.UserIdEqualsValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Documented // 사용자 정의 어노테이션
@Constraint(validatedBy = UserIdEqualsValidator.class) // 검증 로직을 구현한 클래스 지정
@Target({ElementType.PARAMETER, ElementType.FIELD}) // 어노테이션 적용 범위
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
public @interface AuthChatRoom { // 채팅방에 농입인 or 전문가로 속하는 사용자인지 판별

}
