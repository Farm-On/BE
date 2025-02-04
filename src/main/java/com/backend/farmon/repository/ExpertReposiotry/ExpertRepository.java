package com.backend.farmon.repository.ExpertReposiotry;

import com.backend.farmon.domain.Expert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExpertRepository extends JpaRepository<Expert, Long>, ExpertRepositoryCustom {
    // 유저 아이디로 전문가 조회
    Optional<Expert>findExpertByUserId(Long userId);

    // 유저 아이디로 전문가 존재 여부 확인
    boolean existsByUserId(Long userId);

    @Query("SELECT e FROM Expert e ORDER BY e.createdAt ASC")
    Page<Expert> findExpertsWithPaging(Pageable pageable);
}
