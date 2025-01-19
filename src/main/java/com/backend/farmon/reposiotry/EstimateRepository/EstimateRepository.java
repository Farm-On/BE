package com.backend.farmon.reposiotry.EstimateRepository;

import com.backend.farmon.domain.Estimate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstimateRepository extends JpaRepository<Estimate, Long>, EstimateRepositoryCustom {
}
