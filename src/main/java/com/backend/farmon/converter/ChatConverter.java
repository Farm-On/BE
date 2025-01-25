package com.backend.farmon.converter;

import com.backend.farmon.domain.*;
import com.backend.farmon.dto.chat.ChatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

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

    public static ChatResponse.ChatRoomCreateDTO toChatRoomCreateDTO(ChatRoom chatRoom, User farmer){
        return ChatResponse.ChatRoomCreateDTO.builder()
                .chatRoomId(chatRoom.getId())
                .name(chatRoom.getFarmer().getUserName())
//                .profileImage(chatRoom.getFarmer().getProfileImageUrl())
                .type("농업인")
                .averageResponseTime(farmer.getChatAverageResponseTime())
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
//                    .profileImage(chatRoom.getFarmer().getProfileImageUrl())
                    .type("농업인")
                    .lastEnterTime(ConvertTime.convertLocalDatetimeToTime(chatRoom.getFarmerLastEnter()))
                    .build();
        }

        return ChatResponse.ChatRoomEnterDTO.builder()
                .name(chatRoom.getExpert().getUser().getUserName())
//                .profileImage(chatRoom.getExpert().getUser().getProfileImageUrl())
                .type("전문가")
                .lastEnterTime(ConvertTime.convertLocalDatetimeToTime(chatRoom.getExpertLastEnter()))
                .build();
    }

    public static ChatResponse.ChatMessageListDTO toChatMessageListDTO(Slice<ChatMessage> chatMessages, Long userId) {
        // 채팅 메시지 리스트를 ChatMessageDetailDTO로 변환
        List<ChatResponse.ChatMessageDetailDTO> chatMessageDetailDTOList = chatMessages.stream()
                .map(chatMessage -> toChatMessageDetailDTO(chatMessage, userId))
                .toList();

        return ChatResponse.ChatMessageListDTO.builder()
                .chatMesageList(chatMessageDetailDTOList)
                .chatMessageListSize(chatMessages.getNumberOfElements())
                .isFirst(chatMessages.isFirst())
                .isLast(chatMessages.isLast())
                .hasNext(chatMessages.hasNext())
                .build();
    }

    public static ChatResponse.ChatMessageDetailDTO toChatMessageDetailDTO(ChatMessage chatMessage, Long userId){
        return ChatResponse.ChatMessageDetailDTO.builder()
                .messageContent(chatMessage.getContent())
                .isOtherRead(chatMessage.getIsRead())
                .isMine(chatMessage.getSenderId().equals(userId))
                .sendTime(ConvertTime.convertToAmPmFormat(chatMessage.getCreatedAt()))
                .build();
    }

    public static ChatResponse.ChatRoomListDTO toChatRoomListDTO(Page<ChatRoom> chatRoomPage, List<ChatResponse.ChatRoomDetailDTO> chatRoomInfoList){

        return ChatResponse.ChatRoomListDTO.builder()
                .chatRoomInfoList(chatRoomInfoList)
                .chatRoomInfoListSize(chatRoomInfoList.size())
                .totalPage(chatRoomPage.getTotalPages())
                .totalElements(chatRoomPage.getTotalElements())
                .isFirst(chatRoomPage.isFirst())
                .isLast(chatRoomPage.isLast())
                .build();
    }

    public static ChatResponse.ChatRoomDetailDTO toChatRoomDetailDTO(
            ChatRoom chatRoom, ChatMessage chatMessage, Area area, Boolean isExpert, Integer unReadMessageCount) {

        User user = isExpert
                ? chatRoom.getExpert().getUser()
                : chatRoom.getFarmer();

        return ChatResponse.ChatRoomDetailDTO.builder()
                .chatRoomId(chatRoom.getId())
                .name(user.getUserName())
//                .profileImage(user.getProfileImageUrl())
                .estimateBudget(chatRoom.getEstimate().getBudget())
                .estimateCategory(chatRoom.getEstimate().getCategory())
                .estimateAreaName(area.getAreaName())
                .estimateAreaName(area.getAreaNameDetail())
                .unreadMessageCount(unReadMessageCount)
                .lastMessageContent(chatMessage != null ? chatMessage.getContent() : null) // null-safe 처리
                .lastMessageDate(chatMessage != null ? ConvertTime.convertToYearMonthDay(chatMessage.getCreatedAt()) : null) // null-safe 처리
                .build();
    }

    public static ChatResponse.ChatRoomEstimateDTO toChatRoomEstimateDTO(Estimate estimate, List<EstimateImage> estimateImageList){
        // 견적과 연관된 지역
        Area area = estimate.getArea();

        return ChatResponse.ChatRoomEstimateDTO.builder()
                .estimateCropCategory(estimate.getCrop().getCategory())
                .estimateCropName(estimate.getCrop().getName())
                .estimateApplyName(estimate.getUser().getUserName())
                .estimateCategory(estimate.getCategory())
                .estimateAreaName(area.getAreaName())
                .estimateAreaDetail(area.getAreaNameDetail())
                .estimateBudget(estimate.getBudget())
                .estimateContent(estimate.getBody())
                // map을 사용하여 imageUrl 리스트로 변환
                .estimateImageList(
                        estimateImageList.stream()
                                .map(EstimateImage::getImageUrl)
                                .toList()
                )
                .build();
    }
}
