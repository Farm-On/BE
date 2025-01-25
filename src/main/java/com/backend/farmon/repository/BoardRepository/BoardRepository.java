package com.backend.farmon.repository.BoardRepository;

import com.backend.farmon.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
}