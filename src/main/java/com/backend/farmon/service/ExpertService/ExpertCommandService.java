package com.backend.farmon.service.ExpertService;

import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.ExpertCareer;
import com.backend.farmon.domain.PortfolioImg;
import com.backend.farmon.dto.expert.ExpertCareerRequest;
import com.backend.farmon.dto.expert.ExpertProfileRequest;
import com.backend.farmon.dto.expert.PortfolioRequest;
import com.backend.farmon.dto.expert.PortfolioResponse;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExpertCommandService {
    SignupResponse.ExpertJoinResultDTO joinExpert(Long userId, SignupRequest.ExpertJoinDto request);
    ExpertCareer postExpertCareer(Long expertId, ExpertCareerRequest.ExpertCareerPostDTO request);
    Expert updateExpertSpecialty(Long expertId, ExpertProfileRequest.UpdateSpecialtyDTO request);
    Expert updateExpertArea(Long expertId, ExpertProfileRequest.UpdateAreaDTO request);
    PortfolioResponse.PostPortfolioResultDTO savePortfolio(Long expertId, PortfolioRequest.PostPortfolioDTO postPortfolioDTO,
                                                           List<MultipartFile> ImgList, MultipartFile thumbnailImg);
    String updateTextWithImageUrls(String text, List<PortfolioImg> imageUrls);
}
