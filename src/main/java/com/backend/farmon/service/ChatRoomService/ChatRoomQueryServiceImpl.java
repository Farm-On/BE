package com.backend.farmon.service.ChatRoomService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.EstimateHandler;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.repository.ChatMessageRepository.ChatMessageRepository;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomQueryServiceImpl implements ChatRoomQueryService {
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;

    private static final Integer PAGE_SIZE=10;

    // 페이지 정렬, 최신순
    private Pageable pageRequest(Integer pageNumber) {
        return PageRequest.of(pageNumber, PAGE_SIZE, Sort.by("createdAt").descending());
    }

    // 채팅방 목록 조회
    @Override
    public ChatResponse.ChatRoomListDTO findChatRoom(Long userId, Integer read, Integer pageNumber) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EstimateHandler(ErrorStatus.USER_NOT_FOUND));

        // 안 읽음 필터링에 따른 유저의 모든 채팅방 목록을 페이지네이션으로 조회
        Page<ChatRoom> chatRoomPage = read.equals(1)
                ? chatRoomRepository.findUnReadChatRoomsByUserId(userId, pageRequest(pageNumber))
                : chatRoomRepository.findChatRoomsByUserId(userId, pageRequest(pageNumber));

        log.info("안 읽음 필터링에 따른 유저의 모든 채팅방 목록 페이지네이션 조회 완료 - userId: {}", userId);

        // 채팅 대화방 세부 정보 목록 생성
        List<ChatResponse.ChatRoomDetailDTO> chatRoomInfoList = chatRoomPage.stream().map(chatRoom -> {
            boolean isExpert = chatRoom.getExpert().getId().equals(userId);

            // 안 읽은 채팅 메시지 개수 조회
            int unReadMessageCount = chatMessageRepository.findByChatRoomIdAndIsReadFalse(chatRoom.getId()).size();
            log.info("안 읽은 채팅 메시지 개수 조회 완료 - userId: {}, 안 읽은 메시지 개수: {}", userId, unReadMessageCount);

            // 채팅방과 일치하는 최신 메시지 조회
            ChatMessage lastMessage = chatMessageRepository
                    .findFirstByChatRoomIdOrderByCreatedAtDesc(chatRoom.getId())
                    .orElse(null);

            log.info("채팅방과 일치하는 최신 메시지 조회 완료 - userId: {}, 내용: {}", userId, lastMessage.getContent());

            // 채팅방 대화방 세부 정보를 dto로 변환
            return ChatConverter.toChatRoomDetailDTO(chatRoom, lastMessage, isExpert, unReadMessageCount);
        }).toList();

        // 최종 채팅방 목록 정보 DTO 생성 및 반환
        return ChatConverter.toChatRoomListDTO(chatRoomPage, chatRoomInfoList);
    }

    // 채팅방의 견적 조회
    @Override
    public ChatResponse.ChatRoomEstimateDTO findChatRoomEstimate(Long userId, Long chatRoomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EstimateHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EstimateHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        Estimate estimate = estimateRepository.findEstimateWithImages(chatRoom.getEstimate().getId());
        log.info("채팅방의 견적 조회 완료 - userId: {}, estimateId: {}", userId, estimate.getId());

        return ChatConverter.toChatRoomEstimateDTO(estimate, estimate.getEstimateImageList());
    }
}
