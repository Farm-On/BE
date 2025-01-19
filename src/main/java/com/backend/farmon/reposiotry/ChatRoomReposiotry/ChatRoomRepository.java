package com.backend.farmon.reposiotry.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
}
