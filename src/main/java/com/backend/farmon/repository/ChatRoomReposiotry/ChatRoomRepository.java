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
    //estimateId로 매핑된 채팅방 찾기
    @Query("SELECT cr FROM ChatRoom cr WHERE cr.estimate.id = :estimateId " +
            "ORDER BY cr.createdAt DESC")
    Page<ChatRoom> findChatRoomByEstimateId(@Param("estimateId") Long estimateId, Pageable pageable);

    // 채팅방에서 전문가 여부
//    @Query("SELECT CASE WHEN COUNT(e) > 0 THEN TRUE ELSE FALSE END " +
//            "FROM ChatRoom cr " +
//            "JOIN cr.expert e " +
//            "WHERE cr.id = :chatRoomId AND e.user.id = :userId")
//    Boolean isExpertInChatRoom(@Param("userId") Long userId, @Param("chatRoomId") Long chatRoomId);
}
