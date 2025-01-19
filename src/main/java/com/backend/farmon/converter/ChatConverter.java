package com.backend.farmon.converter;

import com.backend.farmon.domain.*;
import com.backend.farmon.dto.chat.ChatResponse;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ChatConverter {
    public static ChatRoom toChatRoom(Expert expert, Estimate estimate, User farmer){
        return ChatRoom.builder()
                .expertLastEnter(LocalDateTime.now())
                .farmer(farmer)
                .expert(expert)
                .estimate(estimate)
                .build();
    }

    public static ChatResponse.ChatRoomCreateDTO toChatRoomCreateDTO(ChatRoom chatRoom){
        return ChatResponse.ChatRoomCreateDTO.builder()
                .chatRoomId(chatRoom.getId())
                .name(chatRoom.getFarmer().getUserName())
                .profileImage(chatRoom.getFarmer().getProfileImageUrl())
                .type("농업인")
                .lastEnterTime(null)
                .averageResponseTime(null)
                .build();
    }

    public static ChatResponse.ChatRoomDeleteDTO toChatRoomDeleteDTO(){
        return ChatResponse.ChatRoomDeleteDTO.builder()
                .isDeleteSuccess(true)
                .build();
    }

    public static ChatResponse.ChatRoomEnterDTO toChatRoomEnterDTO(ChatRoom chatRoom, String userType) {
        boolean isExpert = userType.equals("전문가");

        if (isExpert) {
            return ChatResponse.ChatRoomEnterDTO.builder()
                    .name(chatRoom.getFarmer().getUserName())
                    .profileImage(chatRoom.getFarmer().getProfileImageUrl())
                    .type("농업인")
                    .lastEnterTime(ConvertTime.convertLocalDatetimeToTime(chatRoom.getFarmerLastEnter()))
                    .averageResponseTime(chatRoom.getFarmer().getChatAverageResponseTime())
                    .build();
        }

        return ChatResponse.ChatRoomEnterDTO.builder()
                .name(chatRoom.getExpert().getUser().getUserName())
                .profileImage(chatRoom.getExpert().getUser().getProfileImageUrl())
                .type("전문가")
                .lastEnterTime(ConvertTime.convertLocalDatetimeToTime(chatRoom.getExpertLastEnter()))
                .averageResponseTime(chatRoom.getExpert().getUser().getChatAverageResponseTime())
                .build();
    }

    public static ChatResponse.ChatMessageListDTO toChatMessageListDTO(List<ChatMessage> chatMessages, Long userId) {
        // 채팅 메시지 리스트를 ChatMessageDetailDTO로 변환
        List<ChatResponse.ChatMessageDetailDTO> chatMessageDetailDTOList = chatMessages.stream()
                .map(chatMessage -> toChatMessageDetailDTO(chatMessage, userId))
                .toList();

        return ChatResponse.ChatMessageListDTO.builder()
                .chatMesageList(chatMessageDetailDTOList)
                .chatMessageListSize(chatMessageDetailDTOList.size())
                .build();
    }

    public static ChatResponse.ChatMessageDetailDTO toChatMessageDetailDTO(ChatMessage chatMessage, Long userId){
        return ChatResponse.ChatMessageDetailDTO.builder()
                .messageContent(chatMessage.getContent())
                .isRead(chatMessage.getIsRead())
                .isMine(chatMessage.getSenderId().equals(userId))
                .sendTime(ConvertTime.convertToAmPmFormat(chatMessage.getCreatedAt()))
                .build();
    }

    public static ChatResponse.ChatRoomListDTO toChatRoomListDto(Page<ChatRoom> chatRoomPage, List<ChatResponse.ChatRoomDetailDTO> chatRoomInfoList){

        return ChatResponse.ChatRoomListDTO.builder()
                .chatRoomInfoList(chatRoomInfoList)
                .chatRoomInfoListSize(chatRoomInfoList.size())
                .totalPage(chatRoomPage.getTotalPages())
                .totalElements(chatRoomPage.getTotalElements())
                .isFirst(chatRoomPage.isFirst())
                .isLast(chatRoomPage.isLast())
                .build();
    }

    public static ChatResponse.ChatRoomDetailDTO toChatRoomDetailDto(
            ChatRoom chatRoom, ChatMessage chatMessage, Boolean isExpert, Integer unReadMessageCount) {

        User user = isExpert
                ? chatRoom.getExpert().getUser()
                : chatRoom.getFarmer();

        return ChatResponse.ChatRoomDetailDTO.builder()
                .chatRoomId(chatRoom.getId())
                .name(user.getUserName())
                .profileImage(user.getProfileImageUrl())
                .estimateBudget(chatRoom.getEstimate().getBudget())
                .estimateCategory(chatRoom.getEstimate().getCategory())
                .estimateAddress(chatRoom.getEstimate().getAddress())
                .unreadMessageCount(unReadMessageCount)
                .lastMessageContent(chatMessage != null ? chatMessage.getContent() : null) // null-safe 처리
                .lastMessageDate(chatMessage != null ? ConvertTime.convertToYearMonthDay(chatMessage.getCreatedAt()) : null) // null-safe 처리
                .build();
    }

}
