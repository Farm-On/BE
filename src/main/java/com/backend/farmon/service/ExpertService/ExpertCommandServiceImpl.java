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
import com.backend.farmon.repository.PortfolioImgRepository.PortfolioImgRepository;
import com.backend.farmon.repository.PortfolioRepository.PortfolioRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import com.backend.farmon.service.AWS.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
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
    private final PortfolioImgRepository portfolioImgRepository;

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

    // 전문가 내 프로필 변경 로직
    @Override
    @Transactional
    public Expert updateExpertProfile(Long expertId, ExpertProfileRequest.UpdateProfileDTO request) {
        Expert expert = expertRepository.findById(expertId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        if (request.getNickName() != null) {expert.setNickName(request.getNickName());}
        if (request.getIsNickNameOnly() != null) {expert.setIsNickNameOnly(request.getIsNickNameOnly());}
        if (request.getExpertDescription() != null) {expert.setExpertDescription(request.getExpertDescription());}

        return expertRepository.save(expert);
    }

    // 포트폴리오 등록 서비스
    @Override
    @Transactional
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
        Portfolio savedPortfolio = portfolioRepository.save(newPortfolio);

        // 7. 결과 반환
        return ExpertConverter.toPortfolioGetResultDTO(savedPortfolio);
    }

    @Override
    @Transactional
    public String updateTextWithImageUrls(String text, List<PortfolioImg> imageUrls) {
        // HTML 텍스트를 JSoup으로 파싱
        Document doc = Jsoup.parse(text);

        // 모든 <img> 태그를 가져오기
        Elements imgTags = doc.select("img");

        // 이미지 태그 교체
        int imageIndex = 0;
        for (Element imgTag : imgTags) {
            if (imageIndex < imageUrls.size()) {
                // 현재 이미지 URL을 imageUrls 리스트에서 가져옴
                String s3Url = imageUrls.get(imageIndex).getImageUrl();
                imgTag.attr("src", s3Url);  // src 속성을 새 URL로 교체
                imageIndex++;
            }
        }

        // 변경된 HTML을 문자열로 반환
        return doc.html();
    }

    // 포트폴리오 등록 서비스
    @Override
    @Transactional
    public PortfolioResponse.PostPortfolioResultDTO updatePortfolio(Long portfolioId, PortfolioRequest.PostPortfolioDTO postPortfolioDTO,
                                                                  List<MultipartFile> ImgList, MultipartFile thumbnailImg) {
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new ExpertHandler(ErrorStatus.PORTFOLIO_NOT_FOUND));

        // 1. 썸네일 이미지 수정
        if(thumbnailImg != null){
            // 기존 이미지 S3에서 삭제
            String thumbnailUrl = portfolio.getThumbnailImg();
            String s3key = thumbnailUrl.substring(thumbnailUrl.indexOf("Portfolio/"));
            s3Service.deleteImg(s3key);

            String thumbnailImgUrl = s3Service.putPortfolioImg(thumbnailImg);
            portfolio.setThumbnailImg(thumbnailImgUrl);
        }

        // 2. 제목 수정
        portfolio.setTitle(postPortfolioDTO.getTitle());

        // 3. 새로운 본문 이미지도 추가된 경우
        if(ImgList != null){
            // 새로운 이미지 파일 처리
            List<PortfolioImg> portfolioImgs = new ArrayList<>();
            for (MultipartFile img : ImgList) {
                String s3ImageUrl = s3Service.putPortfolioImg(img);
                PortfolioImg portfolioImg = PortfolioImg.builder() // 포트폴리오 이미지 엔티티 생성
                        .imageUrl(s3ImageUrl)
                        .build();
                portfolioImg.setPortfolio(portfolio); // 포트폴리오 엔티티와 양방매핑
                portfolioImgs.add(portfolioImg);
            }
            portfolioImgRepository.saveAll(portfolioImgs);

            // 본문 이미지 URL 수정
            String updatedText = updateImageSrcWithS3(postPortfolioDTO.getText(), portfolioImgs);
            portfolio.setText(updatedText); // 수정된 본문 텍스트 저장
        }else{ // 4. 텍스트만 바뀐 경우
            portfolio.setText(postPortfolioDTO.getText());
        }
        portfolioRepository.save(portfolio);
        // 결과 반환
        return ExpertConverter.toPortfolioGetResultDTO(portfolio);
    }

    @Override
    @Transactional
    public String updateImageSrcWithS3(String text, List<PortfolioImg> newImageUrls) {
        int imageIndex = 0;
        Document document = Jsoup.parse(text);

        for (Element img : document.select("img")) {
            String src = img.attr("src");

            if (!src.startsWith("https://umcfarmon.s3.ap-northeast-2.amazonaws.com") && imageIndex < newImageUrls.size()) {
                img.attr("src", newImageUrls.get(imageIndex).getImageUrl());
                imageIndex++;
            }
        }
        return document.html();
    }

    // 포트폴리오 삭제 서비스
    @Override
    @Transactional
    public PortfolioResponse.DeletePortfolioResultDTO deletePortfolio(Long portfolioId){
        Portfolio portfolio = portfolioRepository.findById(portfolioId)
                .orElseThrow(() -> new PortfolioHandler(ErrorStatus.PORTFOLIO_NOT_FOUND));

        try {
            // 썸네일 이미지 s3에서 삭제
            String thumbnailUrl = portfolio.getThumbnailImg();
            String s3key = thumbnailUrl.substring(thumbnailUrl.indexOf("Portfolio/"));
            s3Service.deleteImg(s3key);

            // 본문 이미지 s3에서 삭제
            List<String> imgUrls = portfolio.getPortfolioImgList().stream()
                    .map(PortfolioImg::getImageUrl)
                    .collect(Collectors.toList());

            for (String imgUrl : imgUrls) {
                String s3url = imgUrl.substring(imgUrl.indexOf("Portfolio/"));
                s3Service.deleteImg(s3url);
            }

            // 포트폴리오 삭제
            portfolioRepository.delete(portfolio);

            return PortfolioResponse.DeletePortfolioResultDTO.builder()
                    .portfolioId(portfolioId)
                    .build();
        } catch (Exception e) {
            throw new PortfolioHandler(ErrorStatus.PORTFOLIO_DELETE_FAILED);
        }
    }
}