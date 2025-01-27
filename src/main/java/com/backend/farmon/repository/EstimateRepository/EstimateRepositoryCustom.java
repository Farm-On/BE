package com.backend.farmon.repository.EstimateRepository;

import com.backend.farmon.domain.Estimate;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface EstimateRepositoryCustom {
    Page<Estimate> findFilteredEstimates(String estimateName,String budget, String areaName, String areaNameDetail, Pageable pageable);
}
