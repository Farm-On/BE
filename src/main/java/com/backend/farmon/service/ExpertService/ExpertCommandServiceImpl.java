package com.backend.farmon.service.ExpertService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.AreaHandler;
import com.backend.farmon.apiPayload.exception.handler.CropHandler;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.converter.ExpertConverter;
import com.backend.farmon.converter.SignupConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.dto.expert.ExpertCareerRequest;
import com.backend.farmon.dto.expert.ExpertCareerResponse;
import com.backend.farmon.dto.expert.ExpertDetailRequest;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import com.backend.farmon.repository.AreaRepository.AreaRepository;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.ExpertCareerRepository.ExpertCareerRepository;
import com.backend.farmon.repository.ExpertDetailRepository.ExpertDetailRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.UserService.UserCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExpertCommandServiceImpl implements ExpertCommandService {

    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;
    private final PasswordEncoder passwordEncoder;
    private final AreaRepository areaRepository;
    private final CropRepository cropRepository;
    private final ExpertCareerRepository expertCareerRepository;
    private final ExpertDetailRepository expertDetailRepository;

    // 전문가 회원가입 로직
    @Override
    @Transactional
    public SignupResponse.ExpertJoinResultDTO joinExpert(SignupRequest.ExpertJoinDto request) {
        // User 엔티티 생성 및 저장
        Boolean emailExists = userRepository.existsByEmail(request.getEmail());
        if(emailExists) {
            throw new UserHandler(ErrorStatus.EMAIL_ALREADY_EXIST);
        }

        User newUser = SignupConverter.toExpertUser(request);
        newUser.encodePassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(newUser);

        // Expert 엔티티 생성 및 저장
        Area area = areaRepository.findByAreaNameDetail(request.getExpertLocation())
                .orElseThrow(() -> new AreaHandler(ErrorStatus.AREA_NOT_FOUND));

        Crop crop = cropRepository.findByName(request.getExpertCrop())
                .orElseThrow(() -> new CropHandler(ErrorStatus.CROP_NOT_FOUND));

        Expert newExpert = Expert.builder()
            .build();
        newExpert.setCrop(crop);
        newExpert.setArea(area);
        newExpert.setUser(newUser);
        expertRepository.save(newExpert);

        // 전문가 회원가입 응답 DTO생성
        return SignupConverter.toExpertJoinResultDTO(newUser.getId(), newExpert.getId());
    }

    // 전문가 경력 등록 로직
    @Override
    @Transactional
    public ExpertCareer postExpertCareer(Long expertId, ExpertCareerRequest.ExpertCareerPostDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        ExpertCareer newExpertCareer = ExpertConverter.toExpertCareer(request);
        newExpertCareer.setExpert(expert);

        return expertCareerRepository.save(newExpertCareer); // 완성된 엔티티 반환
    }

    // 전문가 추가정보 등록 로직
    @Override
    @Transactional
    public ExpertDatail postExpertDetail(Long expertId, ExpertDetailRequest.ExpertDetailPostDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        ExpertDatail newExpertDetail = ExpertConverter.toExpertDetail(request);
        newExpertDetail.setExpert(expert);

        return expertDetailRepository.save(newExpertDetail); // 완성된 엔티티 반환
    }
}