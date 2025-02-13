package com.backend.farmon.validaton.annotation;

import com.backend.farmon.validaton.validator.UserExistValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

// 유저 존재 검증
@Documented // 사용자 정의 어노테이션
@Constraint(validatedBy = UserExistValidator.class) // 검증 로직을 구현한 클래스 지정
@Target({ElementType.PARAMETER, ElementType.FIELD}) // 어노테이션 적용 범위
@Retention(RetentionPolicy.RUNTIME) // 런타임까지 유지
public @interface ExistUser {
    String message() default "아이디와 일치하는 사용자가 없습니다."; // 기본 에러 메시지

    Class<?>[] groups() default {}; // 유효성 검사 그룹

    Class<? extends Payload>[] payload() default {}; // 메타데이터 전달용
}
