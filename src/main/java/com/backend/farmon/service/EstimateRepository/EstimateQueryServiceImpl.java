package com.backend.farmon.service.EstimateRepository;

import com.backend.farmon.domain.Estimate;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimateQueryServiceImpl implements EstimateQueryService {

    private final EstimateRepository estimateRepository;
    /**
     * Read(상세 조회) - 단건
     */
    @Override
    public EstimateResponseDTO.DetailDTO getEstimateDetail(Long estimateId) {
        // 1)견적 찾기
        Estimate estimate = estimateRepository.findById(estimateId).orElseThrow(() -> new IllegalArgumentException("해당 id와 일치하는 견적서가 없습니다."));

        return EstimateResponseDTO.DetailDTO.builder()
                .estimateId(estimateId)
                .userId(estimate.getUser().getId())
                .userName(estimate.getUser().getUserName())
                .cropName(estimate.getCrop().getName())
                .cropCategory(estimate.getCrop().getCategory())
                .category(estimate.getCategory())
                .address(estimate.getAddressDetail())
                .budget(estimate.getBudget())
                .body(estimate.getBody())
                .createDate(estimate.getCreatedAt().toLocalDate())
                .build();

    }

}
