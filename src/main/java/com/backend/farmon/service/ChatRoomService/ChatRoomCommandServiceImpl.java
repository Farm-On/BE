package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.EstimateHandler;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.reposiotry.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.reposiotry.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.reposiotry.EstimateRepository.EstimateRepository;
import com.backend.farmon.reposiotry.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.reposiotry.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomCommandServiceImpl implements ChatRoomCommandService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;

    // 채팅방 생성
    @Transactional
    @Override
    public ChatResponse.ChatRoomCreateDTO addChatRoom(Long userId, Long estimateId) {
        // 채팅 신청한 전문가
        Expert expert = expertRepository.findById(userId)
                .orElseThrow(()-> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 채팅 신청한 견적
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.ESTIMATE_NOT_FOUND));

        // 견적을 신청한 농업인
        User farmer = userRepository.findById(estimate.getUser().getId())
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = ChatConverter.toChatRoom(expert, estimate, farmer);

//        chatRoom.setExpert(expert);
//        cahtRoom.setFarmer(farmer);
//        chatRoom.setEstimate(estimate);

        return ChatConverter.toChatRoomCreateDTO(chatRoom);
    }

    // 채팅방 입장
    @Override
    public ChatResponse.ChatRoomEnterDTO updateLastEnterTime(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EstimateHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EstimateHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        boolean isExpert = chatRoom.getExpert().getId().equals(userId);
        LocalDateTime now = LocalDateTime.now();

        // 마지막 채팅방 접속 시간 변경
        if (isExpert) {
            chatRoom.setExpertLastEnter(now);
        } else {
            chatRoom.setFarmerLastEnter(now);
        }

        // 안 읽은 메시지들을 읽음 처리
        chatMessageRepository.updateMessagesToReadByChatRoomId(chatRoomId);

        String userType = isExpert ? "전문가" : "농업인";
        return ChatConverter.toChatRoomEnterDTO(chatRoom, userType);
    }

    // 채팅 메시지 내역 조회
    @Override
    public ChatResponse.ChatMessageListDTO findChatMessageList(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        // 모든 채팅 메시지 내역 조회 (EXIT, COMPLETE 제외)
        List<ChatMessage> chatMessageList = chatMessageRepository.findNonExitCompleteMessagesByChatRoomId(chatRoomId);

        return ChatConverter.toChatMessageListDTO(chatMessageList);
    }

    // 채팅방 삭제
    @Transactional
    @Override
    public ChatResponse.ChatRoomDeleteDTO removeChatRoom(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(()-> new EstimateHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        chatRoomRepository.delete(chatRoom);

        return ChatConverter.toChatRoomDeleteDTO();
    }
}
