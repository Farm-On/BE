package com.backend.farmon.service.EstimateRepository;

import com.backend.farmon.domain.Area;
import com.backend.farmon.domain.Crop;
import com.backend.farmon.domain.Estimate;
import com.backend.farmon.domain.User;
import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class EstimateCommandServiceImpl implements EstimateCommandService {
    //private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;

    /**
     * Create(견적서 작성)
     */
    User user = User.builder().build();
    Crop crop = Crop.builder().build();
    Area area = Area.builder().build();
    @Override
    public EstimateResponseDTO.CreateDTO createEstimate(EstimateRequestDTO.CreateDTO requestDTO) {
        // 1) Entity 생성
        Estimate estimate = Estimate.builder()
                .category(requestDTO.getCategory())
                .area(area)
                .addressDetail(requestDTO.getAddressDetail())
                .budget(requestDTO.getBudget())
                .body(requestDTO.getBody())
                .status(0) //기본 상태
                .user(user)
                .crop(crop)
                .build();

        // 2) 이미지 추가(추후에 작성)

        // 3) 견적서 저장
        Estimate savedEstimate = estimateRepository.save(estimate);

        // 4) Response 생성
        return EstimateResponseDTO.CreateDTO.builder()
                .estimateId(savedEstimate.getId())
                .userId(savedEstimate.getUser().getId())
                .build();
    }
    /**
     * Update(견적서 수정)
     */
    @Override
    public EstimateResponseDTO.UpdateDTO updateEstimate(
            Long estimateId, EstimateRequestDTO.UpdateDTO requestDTO) {

        // 1) 견적서 조회
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 estimate id와 일치하는 견적서가 존재하지 않습니다."));

        // 2) 변경 (실제 수정 필드만 변경)
        if (requestDTO.getCategory() != null) {
            estimate.setCategory(requestDTO.getCategory());
        }

        if (requestDTO.getAddressDetail() != null) {
            estimate.setAddressDetail(requestDTO.getAddressDetail());
        }

        if (requestDTO.getBudget() != null) {
            estimate.setBudget(requestDTO.getBudget());
        }

        if (requestDTO.getBody() != null) {
            estimate.setBody(requestDTO.getBody());
        }

        if (requestDTO.getCropId() != null) {
            Crop crop = Crop.builder().build();
            estimate.setCrop(crop);
        }

        // 3) 수정사항 저장
        estimateRepository.save(estimate);

        return EstimateResponseDTO.UpdateDTO.builder()
                .estimateId(estimateId)
                .userId(estimate.getUser().getId())
                .build();
    }
    /**
     * Delete(견적서 삭제)
     */
    @Override
    public EstimateResponseDTO.DeleteDTO deleteEstimate(Long estimateId) {

        // 1) 견적서 조회
        Estimate estimate = estimateRepository.findById(estimateId)
                .orElseThrow(() -> new IllegalArgumentException("해당 estimate id와 일치하는 견적서가 존재하지 않습니다."));

        // 2) 견적서 삭제
        estimateRepository.delete(estimate);

        // 3) 결과 dto반환
        return EstimateResponseDTO.DeleteDTO.builder()
                .estimateId(estimateId)
                .deleted(true)
                .build();
    }


}
