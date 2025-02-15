package com.backend.farmon.repository.BoardRepository;

import com.backend.farmon.domain.BoardPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
    // JpaRepository는 기본적으로 save, findById, delete 등의 메서드를 제공
}
