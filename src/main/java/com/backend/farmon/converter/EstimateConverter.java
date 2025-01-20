package com.backend.farmon.converter;

import com.backend.farmon.domain.Area;
import com.backend.farmon.domain.Crop;
import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class EstimateConverter {

    // createDTO -> Estimate Entity
    public Estimate toEntity(EstimateRequestDTO.CreateDTO requestDTO, User user, Crop crop, Area area) {
        return Estimate.builder()
                .category(requestDTO.getCategory())
                .area(area)
                .address(requestDTO.getAddressDetail())
                .budget(requestDTO.getBudget())
                .body(requestDTO.getBody())
                .status(0) // 기본 상태
                .user(user)
                .crop(crop)
                .build();

    }
    // Estimate -> CreateResponseDTO
    public EstimateResponseDTO.CreateDTO toCreateDTO(Estimate estimate) {
        return EstimateResponseDTO.CreateDTO.builder()
                .estimateId(estimate.getId())
                .userId(estimate.getUser().getId())
                .build();
    }

    // Estimate -> UpdateResponseDTO
    public EstimateResponseDTO.UpdateDTO toUpdateDTO(Long estimateId, Estimate estimate) {
        return EstimateResponseDTO.UpdateDTO.builder()
                .estimateId(estimateId)
                .userId(estimate.getUser().getId())
                .build();
    }

    // Estimate -> DeleteResponseDTO
    public EstimateResponseDTO.DeleteDTO toDeleteDTO(Long estimateId) {
        return EstimateResponseDTO.DeleteDTO.builder()
                .estimateId(estimateId)
                .deleted(true)
                .build();
    }

    // Estimate -> DetialResponseDTO
    public EstimateResponseDTO.DetailDTO toDetailDTO(Estimate estimate) {
        return EstimateResponseDTO.DetailDTO.builder()
                .estimateId(estimate.getId())
                .userId(estimate.getUser().getId())
                .userName(estimate.getUser().getUserName())
                .cropName(estimate.getCrop().getName())
                .cropCategory(estimate.getCrop().getCategory())
                .category(estimate.getCategory())
                .address(estimate.getAddress())
                .budget(estimate.getBudget())
                .body(estimate.getBody())
                .createDate(estimate.getCreatedAt().toLocalDate())
                .build();
    }

}
