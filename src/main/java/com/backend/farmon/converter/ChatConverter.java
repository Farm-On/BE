package com.backend.farmon.converter;

import com.backend.farmon.domain.*;
import com.backend.farmon.domain.enums.ChatMessageType;
import com.backend.farmon.dto.chat.ChatRequest;
import com.backend.farmon.dto.chat.ChatResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class ChatConverter {

    public static ChatMessage toChatMessage(ChatRequest.ChatMessageDTO dto, ChatRoom chatRoom){
        return ChatMessage.builder()
                .senderId(dto.getSenderId())
                .type(ChatMessageType.valueOf(dto.getMessageType()))
                .content(Optional.ofNullable(dto.getMessageContent()).orElse(null))
                .isRead(false) // 일단 읽음 여부 false로 리턴, 추후에 다시 구현
                .chatRoom(chatRoom)
                .build();
    }

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
                .type("농업인")
                .build();
    }

    public static ChatResponse.ChatRoomDeleteDTO toChatRoomDeleteDTO(){
        return ChatResponse.ChatRoomDeleteDTO.builder()
                .isDeleteSuccess(true)
                .build();
    }

    public static ChatResponse.ChatRoomDataDTO toChatRoomDataDTO(ChatRoom chatRoom, Boolean isExpert) {
        // 내가 전문가
        if (isExpert) {
            return ChatResponse.ChatRoomDataDTO.builder()
                    .name(chatRoom.getFarmer().getUserName())
//                    .profileImage(chatRoom.getFarmer().getProfileImageUrl())
                    .type("농업인")
                    .lastEnterTime(ConvertTime.convertLocalDatetimeToTime(chatRoom.getFarmerLastEnter()))
                    .isComplete(chatRoom.getIsExpertComplete())
                    .isOtherComplete(chatRoom.getIsFarmerComplete())
                    .isEstimateComplete(chatRoom.getEstimate().getStatus().equals(1))
                    .build();
        }

        return ChatResponse.ChatRoomDataDTO.builder()
                .name(chatRoom.getExpert().getUser().getUserName())
                .nickName(Optional.ofNullable(chatRoom.getExpert())
                        .map(Expert::getNickName)
                        .orElse(null))
                .profileImage(Optional.ofNullable(chatRoom.getExpert())
                        .map(Expert::getProfileImageUrl)
                        .orElse(null))
                .isExpertNickNameOnly(chatRoom.getExpert().getIsNickNameOnly())
                .type("전문가")
                .lastEnterTime(ConvertTime.convertLocalDatetimeToTime(chatRoom.getExpertLastEnter()))
                .isComplete(chatRoom.getIsFarmerComplete())
                .isOtherComplete(chatRoom.getIsExpertComplete())
                .isEstimateComplete(chatRoom.getEstimate().getStatus().equals(1))
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
            ChatRoom chatRoom, ChatMessage chatMessage, Area area, Boolean isExpert, Long unReadMessageCount) {

        // 내가 전문가라면 농업인을 user로 가져오기
        User user = isExpert
                ? chatRoom.getFarmer()
                : chatRoom.getExpert().getUser();

        return ChatResponse.ChatRoomDetailDTO.builder()
                .chatRoomId(chatRoom.getId())
                .name(user.getUserName())
                .nickName(!isExpert
                        ? Optional.ofNullable(user.getExpert()).map(Expert::getNickName).orElse(null)
                        : null) // 내가 전문가가 아니면(내가 농업인) 상대는 전문가
                .isExpertNickNameOnly(!isExpert ? user.getExpert().getIsNickNameOnly() : null)
                .type(isExpert ? "농업인" : "전문가") // 내가 전문가이면 상대 타입은 농업인
                .profileImage(!isExpert
                        ? Optional.ofNullable(user.getExpert())
                        .map(Expert::getProfileImageUrl)
                        .orElse(null)
                        : null)
                .estimateBudget(chatRoom.getEstimate().getBudget())
                .estimateCategory(chatRoom.getEstimate().getCategory())
                .estimateAreaName(area.getAreaName())
                .estimateAreaDetail(area.getAreaNameDetail())
                .unreadMessageCount(unReadMessageCount.intValue())
                .lastMessageContent(Optional.ofNullable(chatMessage)
                        .map(ChatMessage::getContent)
                        .orElse(null))
                .lastMessageDate(Optional.ofNullable(chatMessage)
                        .map(ChatMessage::getCreatedAt)
                        .map(ConvertTime::convertToYearMonthDay)
                        .orElse(null))
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

    public static ChatResponse.ChatImageDTO toChatImageDTO(String chatImageURL){
        return ChatResponse.ChatImageDTO.builder()
                .chatImageURL(chatImageURL)
                .build();
    }
}
