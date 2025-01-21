package com.backend.farmon.repository.EstimateRepository;

import com.backend.farmon.domain.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EstimateRepository extends JpaRepository<Estimate, Long>, EstimateRepositoryCustom {
    @Query("SELECT e FROM Estimate e LEFT JOIN FETCH e.estimateImageList WHERE e.id = :estimateId")
    Optional<Estimate> findEstimateWithImages(@Param("estimateId") Long estimateId);

    // 1) 특정 userId(농업인)가 작성한 견적서 목록 조회
    List<Estimate> findByUserId(Long userId);

    // 2) 특정 expertId(전문가)에게 매핑된 견적서 목록
    List<Estimate> findByExpertId(Long expertId);

    // 3) 특정 status



}
