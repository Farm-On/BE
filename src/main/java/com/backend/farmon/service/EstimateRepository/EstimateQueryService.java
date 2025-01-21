package com.backend.farmon.service.EstimateRepository;

import com.backend.farmon.dto.estimate.EstimateResponseDTO;

public interface EstimateQueryService {
    public EstimateResponseDTO.DetailDTO getEstimateDetail(Long id);
}
