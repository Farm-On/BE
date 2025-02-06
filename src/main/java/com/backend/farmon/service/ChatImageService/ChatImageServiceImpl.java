package com.backend.farmon.service.ChatImageService;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.backend.farmon.apiPayload.ApiResponse;
import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ChatRoomHandler;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.converter.ChatConverter;
import com.backend.farmon.domain.ChatRoom;
import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.chat.ChatResponse;
import com.backend.farmon.repository.ChatRoomReposiotry.ChatRoomRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatImageServiceImpl implements ChatImageService{

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final S3Service s3Service;

    // 채팅용 이미지 업로드
    @Override
    public ChatResponse.ChatImageDTO uploadChatImage(Long userId, Long chatRoomId, MultipartFile imageFile) throws IOException  {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new ChatRoomHandler(ErrorStatus.CHATROOM_NOT_FOUND));

        // 채팅용 이미지 업로드
        String imageURL = s3Service.putChatImage(userId, chatRoomId, imageFile);
        log.info("채팅용 이미지 업로드 성공, 이미지 URL: {}", imageURL);

        return ChatConverter.toChatImageDTO(imageURL);
    }
}
