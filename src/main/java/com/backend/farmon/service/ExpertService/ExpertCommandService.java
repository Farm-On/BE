package com.backend.farmon.service.ExpertService;

import com.backend.farmon.domain.ExpertCareer;
import com.backend.farmon.domain.ExpertDatail;
import com.backend.farmon.dto.expert.ExpertCareerRequest;
import com.backend.farmon.dto.expert.ExpertDetailRequest;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;

public interface ExpertCommandService {
    SignupResponse.ExpertJoinResultDTO joinExpert(SignupRequest.ExpertJoinDto request);
    ExpertCareer postExpertCareer(Long expertId, ExpertCareerRequest.ExpertCareerPostDTO request);
    ExpertDatail postExpertDetail(Long expertId, ExpertDetailRequest.ExpertDetailPostDTO request);
}
