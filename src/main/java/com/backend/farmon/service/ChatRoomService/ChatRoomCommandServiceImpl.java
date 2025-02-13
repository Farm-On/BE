package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ChatRoomHandler;
import com.backend.farmon.apiPayload.exception.handler.EstimateHandler;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.dto.chat.ChatRequest;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.repository.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.ValidationService.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomCommandServiceImpl implements ChatRoomCommandService{
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ExpertRepository expertRepository;
    private final UserRepository userRepository;
    private final UserAuthorizationUtil userAuthorizationUtil;
    private final ValidationService validationService;

    // 채팅방 생성
    @Transactional
    @Override
    public ChatResponse.ChatRoomCreateDTO addChatRoom(Long userId, Long estimateId) {
        // 채팅방 생성은 전문가만 가능
        if(!userAuthorizationUtil.isCurrentUserRoleMatching("EXPERT"))
            throw new ChatRoomHandler(ErrorStatus.CHATROOM_CREATE_ONLY_EXPERT);

        // 채팅 신청한 전문가
        Expert expert = expertRepository.findExpertByUserId(userId)
                .orElseThrow(()-> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 채팅 신청한 견적
        Estimate estimate = validationService.validateEstimate(estimateId);

        // 견적을 신청한 농업인
        User farmer = userRepository.findById(estimate.getUser().getId())
                .orElseThrow(()-> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = ChatConverter.toChatRoom(expert, estimate, farmer);
        chatRoom.setExpert(expert);
        chatRoom.setFarmer(farmer);
        chatRoom.setEstimate(estimate);

        chatRoomRepository.save(chatRoom);

        log.info("채팅방 생성 완료 - chatRoomId: {}, 생성한 전문가 expertId: {}", chatRoom.getId(), expert.getId());

        return ChatConverter.toChatRoomCreateDTO(chatRoom, farmer);
    }

    // 채팅방 삭제
    @Transactional
    @Override
    public ChatResponse.ChatRoomDeleteDTO removeChatRoom(Long userId, Long chatRoomId) {
        ChatRoom chatRoom = validationService.validateChatRoom(chatRoomId);

        chatMessageRepository.deleteByChatRoomId(chatRoomId); // 채팅 메시지 삭제
        chatRoomRepository.delete(chatRoom); // 채팅방 삭제
        log.info("채팅방 삭제 완료 - chatRoomId: {}", chatRoomId);

        return ChatConverter.toChatRoomDeleteDTO();
    }

    // 채팅방 컨설팅 완료
    @Transactional
    @Override
    public void exchangeChatRoomUserComplete(Long userId, ChatRoom chatRoom, ChatRequest.ChatMessageDTO dto) {
        Long chatRoomId = chatRoom.getId();

        // 채팅방에서의 전문가 여부
        boolean isExpert = !chatRoomRepository.isFarmerInChatRoom(userId, chatRoomId);

        boolean isOtherComplete = false;
        // 컨설팅 완료 여부 변경 및 상대 거래 완료 여부 조회
        if(isExpert){
            chatRoom.setIsExpertComplete(true);
            isOtherComplete = chatRoom.getIsFarmerComplete();
        }
        else{
            chatRoom.setIsFarmerComplete(true);
            isOtherComplete = chatRoom.getIsExpertComplete();
        }

        // 농업인, 전문가 둘 다 컨설팅 완료 일 시 견적 상태 변경
        if(chatRoom.getIsFarmerComplete() && chatRoom.getIsExpertComplete()){
            Estimate estimate = chatRoom.getEstimate();
            estimate.setStatus(1); // 견적 상태 진행 완료로 변경
            estimate.setExpert(chatRoom.getExpert()); // 견적 전문가를 채팅방의 전문가로 변경

            dto.setIsEstimateComplete(true);
        }

        log.info("채팅방 컨설팅 완료 - 역할: {}, userId: {}, chatRoomId: {}",
                isExpert ? "전문가" : "농업인", userId, chatRoomId);
    }

    // 사용자 여부에 따른 채팅 입장 시간 변경
    @Transactional
    @Override
    public void changeChatRoomEnterTime(Long userId, ChatRoom chatRoom) {
        Long chatRoomId = chatRoom.getId();

        // 전문가라면 전문가 접속 시간 뱐걍
        if(!chatRoomRepository.isFarmerInChatRoom(userId, chatRoomId)){
            chatRoom.setExpertLastEnter(LocalDateTime.now());
            log.info("전문가 접속 시간 변경 - 채팅방 아이디: {}", chatRoomId);
        }
        else{
            chatRoom.setFarmerLastEnter(LocalDateTime.now());
            log.info("농업인 접속 시간 변경 - 채팅방 아이디: {}", chatRoomId);
        }
    }
}
