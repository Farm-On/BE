package com.backend.farmon.service.EstimateRepository;


import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;

public interface EstimateCommandService {

    /**
     * Create(견적서 작성)
     */
    public EstimateResponseDTO.CreateDTO createEstimate (EstimateRequestDTO.CreateDTO requestDTO);

    /**
     * Update(견적서 수정)
     */
    public EstimateResponseDTO.UpdateDTO updateEstimate (
            Long estimateId, EstimateRequestDTO.UpdateDTO requestDTO);

    /**
     * Delete(견적서 삭제)
     */
    public EstimateResponseDTO.DeleteDTO deleteEstimate (Long estimateId);
}
