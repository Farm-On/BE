package com.backend.farmon.reposiotry.ChatMessageRepository;

import com.backend.farmon.domain.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>, ChatMessageRepositoryCustom {
}
