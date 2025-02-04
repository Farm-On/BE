package com.backend.farmon.converter;

import com.backend.farmon.domain.*;
import com.backend.farmon.dto.estimate.EstimateRequestDTO;
import com.backend.farmon.dto.estimate.EstimateResponseDTO;
import com.backend.farmon.repository.EstimateRepository.EstimateRepository;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class EstimateConverter {

    // createDTO -> Estimate Entity
    public Estimate toEntity(EstimateRequestDTO.CreateDTO requestDTO, User user, Crop crop, Area area) {
        return Estimate.builder()
                .category(requestDTO.getCategory())
                .area(area)
                .budget(requestDTO.getBudget())
                .title(requestDTO.getTitle())
                .body(requestDTO.getBody())
                .status(0) // 기본 상태
                .user(user)
                .crop(crop)
                .build();

    }
    // Estimate -> CreateResponseDTO
    public EstimateResponseDTO.CreateDTO toCreateResponseDTO(Estimate estimate) {
        return EstimateResponseDTO.CreateDTO.builder()
                .estimateId(estimate.getId())
                .userId(estimate.getUser().getId())
                .build();
    }

    // Estimate -> UpdateResponseDTO
    public EstimateResponseDTO.UpdateDTO toUpdateResponseDTO(Long estimateId, Estimate estimate) {
        return EstimateResponseDTO.UpdateDTO.builder()
                .estimateId(estimateId)
                .userId(estimate.getUser().getId())
                .build();
    }

    // Estimate -> DeleteResponseDTO
    public EstimateResponseDTO.DeleteDTO toDeleteResponseDTO(Long estimateId) {
        return EstimateResponseDTO.DeleteDTO.builder()
                .estimateId(estimateId)
                .deleted(true)
                .build();
    }

    // Estimate -> DetialResponseDTO
    public EstimateResponseDTO.DetailDTO toDetailDTO(Estimate estimate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return EstimateResponseDTO.DetailDTO.builder()
                .estimateId(estimate.getId())
                .userId(estimate.getUser().getId())
                .userName(estimate.getUser().getUserName())
                .cropName(estimate.getCrop().getName())
                .cropCategory(estimate.getCrop().getCategory())
                .category(estimate.getCategory())
                .areaName(estimate.getArea().getAreaName())
                .areaNameDetail(estimate.getArea().getAreaNameDetail())
                .budget(estimate.getBudget())
                .title(estimate.getTitle())
                .body(estimate.getBody())
                .createdDate(estimate.getCreatedAt().toLocalDate().format(formatter))
                .build();
    }

    //Page<Estimate> -> FilteredListDTO
    public EstimateResponseDTO.FilteredListDTO toFilteredListDTO(Page<Estimate> estimatePage, String estimateCategory, String budget, String areaName, String areaNameDetail){
        // 미리보기 리스트로 변환
        List<EstimateResponseDTO.PreviewDTO> previewDTOList = estimatePage.getContent().stream()
                .map(this::toPreviewDTO)
                .collect(Collectors.toList());

        // ListDTO 구성
        return EstimateResponseDTO.FilteredListDTO.builder()
                .listSize(previewDTOList.size())
                .totalPage(estimatePage.getTotalPages())
                .totalElements(estimatePage.getTotalElements())
                .currentPage(estimatePage.getNumber() + 1)
                .isFirst(estimatePage.isFirst())
                .isLast(estimatePage.isLast())
                .estimateCategory(estimateCategory)
                .budget(budget)
                .areaName(areaName)
                .areaNameDetail(areaNameDetail)
                .estimateList(previewDTOList)
                .build();
    }

    //Page<Estimate> -> ListDTO
    public EstimateResponseDTO.ListDTO toListDTO(Page<Estimate> estimatePage){
        // 미리보기 리스트로 변환
        List<EstimateResponseDTO.PreviewDTO> previewDTOList = estimatePage.getContent().stream()
                .map(this::toPreviewDTO)
                .collect(Collectors.toList());

        // ListDTO 구성
        return EstimateResponseDTO.ListDTO.builder()
                .listSize(previewDTOList.size())
                .totalPage(estimatePage.getTotalPages())
                .totalElements(estimatePage.getTotalElements())
                .currentPage(estimatePage.getNumber() + 1)
                .isFirst(estimatePage.isFirst())
                .isLast(estimatePage.isLast())
                .estimateList(previewDTOList)
                .build();
    }
    //List<Estimate> -> ListDTO
    public EstimateResponseDTO.ListDTO toListDTO(List<Estimate> estimateList){
        // 미리보기 리스트로 변환
        List<EstimateResponseDTO.PreviewDTO> previewDTOList = estimateList.stream()
                .map(this::toPreviewDTO)
                .collect(Collectors.toList());

        // ListDTO 구성
        return EstimateResponseDTO.ListDTO.builder()
                .listSize(previewDTOList.size())
                .estimateList(previewDTOList)
                .build();
    }

    //Estimate 엔티티 -> PreviewDTO
    public EstimateResponseDTO.PreviewDTO toPreviewDTO(Estimate estimate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

        return EstimateResponseDTO.PreviewDTO.builder()
                .estimateId(estimate.getId())
                .title(estimate.getTitle())
                .cropName(estimate.getCrop().getName())
                .cropCategory(estimate.getCrop().getCategory())
                .estimateCategory(estimate.getCategory())
                .areaName(estimate.getArea().getAreaName())
                .areaNameDetail(estimate.getArea().getAreaNameDetail())
                .budget(estimate.getBudget())
                .status(estimate.getStatus())
                .createdAt(estimate.getCreatedAt().toLocalDate().format(formatter))
                .build();
    }
    //OfferListDTO로 전환
    public EstimateResponseDTO.OfferListDTO toOfferListDTO(Long estimateId, List<ChatRoom> chatRoomList, EstimateRepository estimateRepository) {
        //제안 리스트로 변환
        List<EstimateResponseDTO.OfferDTO> offerDTOList = chatRoomList.stream()
                .map(chatRoom -> {
                    Long consultingCount = estimateRepository.countByExpert(chatRoom.getExpert());
                    return toOfferDTO(estimateId, chatRoom, consultingCount);
                })
                .collect(Collectors.toList());

        return EstimateResponseDTO.OfferListDTO.builder()
                .listSize(offerDTOList.size())
                .offerList(offerDTOList)
                .build();
    }
    //OfferDTO로 전환
    private EstimateResponseDTO.OfferDTO toOfferDTO(Long estimateId, ChatRoom chatRoom, Long consultingCount) {

        Expert expert = chatRoom.getExpert();
        return EstimateResponseDTO.OfferDTO.builder()
                .estimateId(estimateId)
                .chatRoomId(chatRoom.getId())
                .expertId(expert.getId())
                .name(expert.getUser().getUserName())
                .nickname(expert.getNickName())
                .isNicknameOnly(expert.getIsNickNameOnly())
                .rating(expert.getRating())
                .profileImageUrl(expert.getProfileImageUrl())
                .description(expert.getExpertDescription())
                .consultingCount(consultingCount)
                .build();
    }

    //ExpertCardListDTO 로 변환
    public EstimateResponseDTO.ExpertCardListDTO toExpertCardListDTO(Page<Expert> expertPage) {
       // Expert 엔티티 리스트 -> ExpertCardDTO 리스트로 변환
        List<EstimateResponseDTO.ExpertCardDTO> expertCardDTOList = expertPage.getContent().stream()
                .map(this::toExpertCardDTO)
                .collect(Collectors.toList());

        //ExpertCardListDTO 생성 및 반환
        return EstimateResponseDTO.ExpertCardListDTO.builder()
                .listSize(expertCardDTOList.size())
                .totalPage(expertPage.getTotalPages())
                .totalElements(expertPage.getTotalElements())
                .currentPage(expertPage.getNumber() + 1)
                .isFirst(expertPage.isFirst())
                .isLast(expertPage.isLast())
                .expertCardDTOList(expertCardDTOList)
                .build();
    }
    //ExpertCardDTO 로 변환
    public EstimateResponseDTO.ExpertCardDTO toExpertCardDTO(Expert expert) {
        return EstimateResponseDTO.ExpertCardDTO.builder()
                .expertId(expert.getId())
                .name(expert.getUser().getUserName())
                .nickname(expert.getNickName())
                .isNicknameOnly(expert.getIsNickNameOnly())
                .cropCategory(expert.getCrop().getCategory())
                .cropName(expert.getCrop().getName())
                .rating(expert.getRating())
                .careerYears(expert.getCareerYears())
                .profileImageUrl(expert.getProfileImageUrl())
                .build();
    }

}
