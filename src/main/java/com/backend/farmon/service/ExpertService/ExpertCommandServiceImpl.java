package com.backend.farmon.service.ExpertService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.*;
import com.backend.farmon.config.security.UserAuthorizationUtil;
import com.backend.farmon.converter.ExpertConverter;
import com.backend.farmon.converter.SignupConverter;
import com.backend.farmon.domain.*;
import com.backend.farmon.domain.enums.Role;
import com.backend.farmon.dto.expert.ExpertCareerRequest;
import com.backend.farmon.dto.expert.ExpertProfileRequest;
import com.backend.farmon.dto.expert.PortfolioRequest;
import com.backend.farmon.dto.expert.PortfolioResponse;
import com.backend.farmon.dto.user.SignupRequest;
import com.backend.farmon.dto.user.SignupResponse;
import com.backend.farmon.repository.AreaRepository.AreaRepository;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.ExpertCareerRepository.ExpertCareerRepository;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.PortfolioRepository.PortfolioRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ExpertCommandServiceImpl implements ExpertCommandService {

    private final UserRepository userRepository;
    private final ExpertRepository expertRepository;
    private final AreaRepository areaRepository;
    private final CropRepository cropRepository;
    private final ExpertCareerRepository expertCareerRepository;
    private final UserAuthorizationUtil userAuthorizationUtil;
    private final S3Service s3Service;
    private final PortfolioRepository portfolioRepository;

    // 전문가 등록 로직
    @Override
    @Transactional
    public SignupResponse.ExpertJoinResultDTO joinExpert(Long userId, SignupRequest.ExpertJoinDto request) {
        // 입력받은 유저 아이디 검증
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        // 토큰 검증(전문가 전환을 하려는 유저는 현재 role이 농업인이어야 함)
        userAuthorizationUtil.isCurrentUserMatching(userId, Role.FARMER.toString());

        // 이미 저 유저 아이디로 등록된 전문가 엔티티가 있으면 에러
        if (expertRepository.existsByUserId(userId)) {
            throw new ExpertHandler(ErrorStatus.EXPERT_ALREADY_EXISTS);
        }

        // Expert 엔티티 생성 및 저장
        Area area = areaRepository.findByAreaNameDetail(request.getExpertLocation())
                .orElseThrow(() -> new AreaHandler(ErrorStatus.AREA_NOT_FOUND));

        Crop crop = cropRepository.findByName(request.getExpertCrop())
                .orElseThrow(() -> new CropHandler(ErrorStatus.CROP_NOT_FOUND));

        Expert newExpert = Expert.builder()
            .build();
        newExpert.setCrop(crop);
        newExpert.setArea(area);
        newExpert.setUser(user);
        expertRepository.save(newExpert);

        // 전문가 회원가입 응답 DTO생성
        return SignupConverter.toExpertJoinResultDTO(user.getId(), newExpert.getId());
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

    // 전문가 대표서비스 변경 로직
    @Override
    @Transactional
    public Expert updateExpertSpecialty(Long expertId, ExpertProfileRequest.UpdateSpecialtyDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 입력받은 카테고리가 있고 기존 것과 다르면 업데이트 수행
        if (request.getCrop() != null && !expert.getCrop().getName().equals(request.getCrop())) {
            Crop newCrop = cropRepository.findByName(request.getCrop())
                    .orElseThrow(() -> new CropHandler(ErrorStatus.CROP_NOT_FOUND));
            expert.setCrop(newCrop);
        }
        if (request.getServiceDetail1() != null) {expert.setServiceDetail1(request.getServiceDetail1());}
        if (request.getServiceDetail2() != null) {expert.setServiceDetail2(request.getServiceDetail2());}
        if (request.getServiceDetail3() != null) {expert.setServiceDetail3(request.getServiceDetail3());}
        if (request.getServiceDetail4() != null) {expert.setServiceDetail4(request.getServiceDetail4());}

        return expertRepository.save(expert);
    }

    // 전문가 활동지역 변경 로직
    @Override
    @Transactional
    public Expert updateExpertArea(Long expertId, ExpertProfileRequest.UpdateAreaDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 입력받은 카테고리가 있고 기존 것과 다르면 업데이트 수행
        if (request.getAreaNameDetail() != null && !expert.getArea().getAreaNameDetail().equals(request.getAreaNameDetail())) {
            Area newArea = areaRepository.findByAreaNameDetail(request.getAreaNameDetail())
                    .orElseThrow(() -> new AreaHandler(ErrorStatus.AREA_NOT_FOUND));
            expert.setArea(newArea);
        }
        if (request.getAvailableRange() != null) {expert.setAvailableRange(request.getAvailableRange());}
        if (request.getIsAvailableEverywhere() != null) {expert.setIsAvailableEverywhere(request.getIsAvailableEverywhere());}
        if (request.getIsExcludeIsland() != null) {expert.setIsExcludeIsland(request.getIsExcludeIsland());}

        return expertRepository.save(expert);
    }

    // 포트폴리오 등록 서비스
    public PortfolioResponse.PostPortfolioResultDTO savePortfolio(Long expertId, PortfolioRequest.PostPortfolioDTO postPortfolioDTO,
                                                                      List<MultipartFile> ImgList, MultipartFile thumbnailImg) {
        // 1. 썸네일 이미지 업로드
        String thumbnailImgUrl = s3Service.putPortfolioImg(thumbnailImg);

        // 2. 포트폴리오 엔티티 생성
        Portfolio newPortfolio = Portfolio.builder()
                .thumbnailImg(thumbnailImgUrl)
                .title(postPortfolioDTO.getTitle())
                .text("")
                .build();

        // 3. 전문가와 매핑
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));
        newPortfolio.setExpert(expert);

        // 4. 포트폴리오 이미지 처리 (이미지 파일 리스트)
        List<PortfolioImg> portfolioImgs = new ArrayList<>();
        for (MultipartFile img : ImgList) {
            String s3ImageUrl = s3Service.putPortfolioImg(img);
            PortfolioImg portfolioImg = PortfolioImg.builder() // 포트폴리오 이미지 엔티티 생성
                    .imageUrl(s3ImageUrl)
                    .build();
            portfolioImg.setPortfolio(newPortfolio); // 포트폴리오 엔티티와 양방매핑
            portfolioImgs.add(portfolioImg);
        }

        // 5. 본문 이미지 URL 수정
        String updatedText = updateTextWithImageUrls(postPortfolioDTO.getText(), portfolioImgs);
        newPortfolio.setText(updatedText); // 수정된 본문 텍스트 저장

        // 6. 포트폴리오와 이미지들을 DB에 저장
//        newPortfolio.setPortfolioImgList(portfolioImgs);
        Portfolio savedPortfolio = portfolioRepository.save(newPortfolio);

        // 7. 결과 반환
        return ExpertConverter.toPortfolioGetResultDTO(savedPortfolio);
    }

    public String updateTextWithImageUrls(String text, List<PortfolioImg> imageUrls) {
        // 본문 텍스트에서 이미지 태그를 찾아서 S3 URL로 변경
        StringBuilder updatedText = new StringBuilder(text);
        int imageIndex = 0;

        // 이미지 태그를 찾기 위한 정규 표현식 패턴 정의
        // <img> 태그에서 src 속성만을 추출하는 패턴
        Pattern pattern = Pattern.compile("<img[^>]*src=['\"](http[^\"]*)['\"][^>]*>");

        // text에서 패턴에 맞는 부분을 찾을 수 있는 Matcher 객체 생성
        Matcher matcher = pattern.matcher(updatedText);

        // 텍스트 내의 모든 이미지 태그를 순차적으로 찾아서 교체
        while (matcher.find() && imageIndex < imageUrls.size()) {
            // 현재 이미지 URL을 imageUrls 리스트에서 가져옴
            String s3Url = imageUrls.get(imageIndex).getImageUrl();

            // 이미지 태그를 기존 src URL에서 S3 URL로 교체
            // 예) <img src="http://example.com/old-image.jpg">를 <img src="s3://bucket/path/image.jpg">로 교체
            updatedText.replace(matcher.start(), matcher.end(), "<img src=\"" + s3Url + "\">");

            // imageUrls 리스트에서 다음 이미지를 사용할 수 있도록 인덱스 증가
            imageIndex++;
        }


        return updatedText.toString();
    }


}