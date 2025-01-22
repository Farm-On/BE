package com.backend.farmon.service.UserService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.converter.UserConverter;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.User;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.user.ExchangeResponse;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserQueryServiceImpl implements UserQueryService {
    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;

    @Override
    public ExchangeResponse exchangeRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        // 농업인 -> 전문가 전환 시 전문가로 등록되어 있는 지 확인
        Expert expert = (role.equals(Role.EXPERT))
                ? expertRepository.findExpertByUserId(userId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_REGISTER))
                : null;

        // jwt 사용 시 별도의 로직 필요?

        return UserConverter.toExchangeResponse(userId, role, expert);
    }
}
