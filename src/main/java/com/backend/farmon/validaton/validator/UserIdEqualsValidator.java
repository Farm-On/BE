package com.backend.farmon.validaton.validator;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.validaton.annotation.EqualsUserId;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


// 토큰에 설정되어 있는 userId와 파라미터로 받은 userId가 같은 지 검증
@Component
@RequiredArgsConstructor
public class UserIdEqualsValidator implements ConstraintValidator<EqualsUserId, Long> {
    private final UserAuthorizationUtil userAuthorizationUtil;

    @Override
    public void initialize(EqualsUserId constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long values, ConstraintValidatorContext context) {

        // 토큰에 설정되어 있는 userId와 파라미터로 받은 userId가 같은 지 검증
        boolean isValid = userAuthorizationUtil.isCurrentUserIdMatching(values);

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.AUTHORIZATION_NOT_EQUALS.toString()).addConstraintViolation();
        }

        return isValid;
    }
}
