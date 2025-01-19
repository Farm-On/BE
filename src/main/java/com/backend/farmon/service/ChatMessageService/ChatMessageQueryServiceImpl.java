package com.backend.farmon.service.ChatMessageService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.EstimateHandler;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.domain.ChatMessage;
import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.reposiotry.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.reposiotry.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.reposiotry.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatMessageQueryServiceImpl implements ChatMessageQueryService{
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    // 채팅 메시지 내역 조회
    @Override
    public ChatResponse.ChatMessageListDTO findChatMessageList(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        // 모든 채팅 메시지 내역 조회 (EXIT, COMPLETE 제외)
        List<ChatMessage> chatMessageList = chatMessageRepository.findNonExitCompleteMessagesByChatRoomId(chatRoomId);

        return ChatConverter.toChatMessageListDTO(chatMessageList, userId);
    }
}
