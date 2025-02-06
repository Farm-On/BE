package com.backend.farmon.service.ChatImageService;

import com.backend.farmon.dto.chat.ChatResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ChatImageService {

    // 채팅용 이미지 업로드
    ChatResponse.ChatImageDTO uploadChatImage(Long userId, Long chatRoomId, MultipartFile imageFile) throws IOException;
}
