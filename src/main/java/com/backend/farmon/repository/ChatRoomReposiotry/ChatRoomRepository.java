package com.backend.farmon.repository.ChatRoomReposiotry;

import com.backend.farmon.domain.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long>, ChatRoomRepositoryCustom {
    @Query("SELECT c FROM ChatRoom c LEFT JOIN FETCH c.messageList WHERE c.id = :chatRoomId")
    Optional<ChatRoom> findChatRoomWithMessages(@Param("chatRoomId") Long chatRoomId);

    //estimateId로 매핑된 채팅방 찾기
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.estimate.id = :estimateId " +
            "ORDER BY cr.createdAt DESC")
    Page<ChatRoom> findChatRoomByEstimateId(@Param("estimateId") Long estimateId, Pageable pageable);
}
