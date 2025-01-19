package com.backend.farmon.converter;

import com.backend.farmon.domain.*;
import com.backend.farmon.dto.chat.ChatResponse;

import java.time.LocalDateTime;
import java.util.List;

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
                    .lastEnterTime(ConvertTime.convertToTime(chatRoom.getFarmerLastEnter()))
                    .averageResponseTime(chatRoom.getFarmer().getChatAverageResponseTime())
                    .build();
        }

        return ChatResponse.ChatRoomEnterDTO.builder()
                .name(chatRoom.getExpert().getUser().getUserName())
                .profileImage(chatRoom.getExpert().getUser().getProfileImageUrl())
                .type("전문가")
                .lastEnterTime(ConvertTime.convertToTime(chatRoom.getExpertLastEnter()))
                .averageResponseTime(chatRoom.getExpert().getUser().getChatAverageResponseTime())
                .build();
    }

    public static ChatResponse.ChatMessageListDTO toChatMessageListDTO(List<ChatMessage> chatMessages){
        // 채팅 메시지 리스트를 ChatMessageDetailDTO로 변환
        List<ChatResponse.ChatMessageDetailDTO> chatMessageDetailDTOList = chatMessages.stream()
                .map(ChatConverter::toChatMessageDetailDTO)
                .toList();

        return ChatResponse.ChatMessageListDTO.builder()
                .chatMesageList(chatMessageDetailDTOList)
                .chatMessageListSize(chatMessageDetailDTOList.size())
                .build();
    }

    public static ChatResponse.ChatMessageDetailDTO toChatMessageDetailDTO(ChatMessage chatMessage){
        return ChatResponse.ChatMessageDetailDTO.builder()
                .messageContent(chatMessage.getContent())
                .isRead(chatMessage.getIsRead())
                .isMine(chatMessage.getIsMine())
                .sendTime(ConvertTime.convertToAmPmFormat(chatMessage.getCreatedAt()))
                .build();
    }
}
