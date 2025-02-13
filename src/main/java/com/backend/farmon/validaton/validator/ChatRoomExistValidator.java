package com.backend.farmon.validaton.validator;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.service.ValidationService.ValidationService;
import com.backend.farmon.validaton.annotation.ExistChatRoom;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// 검증 대상은 Long
@RequiredArgsConstructor
@Component
public class ChatRoomExistValidator implements ConstraintValidator<ExistChatRoom,Long> {

    private final ValidationService validationService;

    @Override
    public void initialize(ExistChatRoom constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext context) {
        // 파라미터로 넘어온 채팅방 아이디가 존재하는 아이디인지 검증
        boolean isValid = validationService.existsChatRoomById(value);

        if(!isValid){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorStatus.CHATROOM_NOT_FOUND.toString()).addConstraintViolation();
        }

        return isValid;
    }
}