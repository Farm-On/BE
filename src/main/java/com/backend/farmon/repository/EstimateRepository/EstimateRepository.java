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
}
