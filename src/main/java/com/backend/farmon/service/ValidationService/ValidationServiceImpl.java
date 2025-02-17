package com.backend.farmon.service.ValidationService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ChatRoomHandler;
import com.backend.farmon.apiPayload.exception.handler.EstimateHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.User;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ValidationServiceImpl implements ValidationService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final EstimateRepository estimateRepository;

    public User validateUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
    }

    public ChatRoom validateChatRoom(Long chatRoomId) {
        return chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));
    }

    public Estimate validateEstimate(Long estimateId) {
        return estimateRepository.findById(estimateId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.ESTIMATE_NOT_FOUND));
    }

    // User 존재 여부 검증
    public Boolean existsUserById(Long userId) {
        return userRepository.existsById(userId);
    }

    // 채팅방 존재 여부 검증
    public Boolean existsChatRoomById(Long chatRoomId) {
        return chatRoomRepository.existsById(chatRoomId);
    }

    // 채팅방 권한 검증
    // 채팅방과 연관관계가 있는 사용자인지 & 역할이 일치하지는지 검증
    public void validateAuthInChatRoom(Long userId, Long chatRoomId, String role) {
        String result = chatRoomRepository.checkUserRoleInChatRoom(userId, chatRoomId, role);
        switch (result){
            case "NOT_IN_CHATROOM":
                log.error("해당 채팅방에 농업인 또는 전문가로 속하지 않는 사용자 - userId: {}, chatRoomId: {}, role: {}", userId, chatRoomId, role);
                throw new ChatRoomHandler(ErrorStatus.NOT_CHATROOM_USER);

            case "WRONG_ROLE":
                log.error("로그인한 사용자의 역할과 채팅방에서의 역할 불일치 - userId: {}, chatRoomId: {}, role: {}", userId, chatRoomId, role);
                throw new ChatRoomHandler(ErrorStatus.NOT_EQUALS_CHATROOM_ROLE);
        }
    }
}