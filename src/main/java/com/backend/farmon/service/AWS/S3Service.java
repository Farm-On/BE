package com.backend.farmon.service.AWS;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.ExpertHandler;
import com.backend.farmon.domain.*;
import com.backend.farmon.repository.ExpertReposiotry.ExpertRepository;
import com.backend.farmon.repository.PostRepository.PostImgRepository;
import com.backend.farmon.repository.PostRepository.PostRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class S3Service {

    private final AmazonS3 amazonS3;
    private final PostImgRepository imgRepository;
    private final PostRepository postRepository;
    private final ExpertRepository expertRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 확장자 추출
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }



    @Transactional
    public PostImg saveImage(byte[] imageBytes, Post post) throws Exception {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new IllegalArgumentException("업로드할 파일이 없습니다.");
        }

        // 저장할 파일명 생성 (UUID + 확장자)
        String storedFileName = UUID.randomUUID() + ".jpg"; // Base64 이미지 확장자 지정

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        metadata.setContentType("image/jpeg"); // 기본 이미지 타입 설정

        // S3에 파일 업로드
        try (InputStream inputStream = new ByteArrayInputStream(imageBytes)) {
            amazonS3.putObject(bucket, storedFileName, inputStream, metadata);
        }

        // PostImg 객체 저장
        PostImg img = imgRepository.save(PostImg.builder()
                .originalFileName(storedFileName) // Base64에서 원본 파일명을 알 수 없으므로 저장된 파일명 사용
                .storedFileName(storedFileName)
                .build());

        img.changePost(post); // 게시글과 연결
        return img;
    }

    // Amazon S3에서 다운로드 가능한 URL로 변환해 반환합니다. 반환된 URL을 통해 사용자는 이미지를 직접 다운로드할 수 있습니다.
    public List<ResponseEntity<UrlResource>> downloadImg(Long postId) {
        // boardId에 해당하는 게시글이 없으면 null return
        Post post = postRepository.findById(postId).orElseThrow(()->new RuntimeException("게시글이 확인되지 않습니다."));

        if (post.getPostImgs() == null) {
            return null;
        }

        List<ResponseEntity<UrlResource>> entityList = new ArrayList<>();

        List<PostImg> imgs = post.getPostImgs();
        for (PostImg img : imgs) {
            UrlResource urlResource = new UrlResource(amazonS3.getUrl(bucket, img.getStoredFileName()));

            // 올려져 있는 파일명이 한글인 경우, 이 작업을 안해주면 한글이 깨질 수 있음
            String encodedUploadFileName = UriUtils.encode(img.getStoredFileName(), StandardCharsets.UTF_8);
            String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

            // header에 CONTENT_DISPOSITION 설정을 통해 클릭 시 다운로드 진행
            entityList.add(ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                    .body(urlResource));
        }
        return entityList;
    }
    // 해당 파일명에 해당하는 이미지의 S3 URL 주소 반환
    public String getFullPath(String filename) {
        return amazonS3.getUrl(bucket, filename).toString();
    }

    // 해당 파일명들에 해당하는 이미지의 S3 URL 주소 반환
    public List<String> getFullPath(List<PostImg> imgs) {
        List<String> filenames = new ArrayList<>();
        for (PostImg img : imgs){
            filenames.add(amazonS3.getUrl(bucket, img.getStoredFileName()).toString());
        }
        return filenames;
    }

    // 해당 이미지Id 로 삭제
    @Transactional
    public void deleteImage(Long imgId)  {
        PostImg img = imgRepository.findById(imgId).orElseThrow(() -> new RuntimeException("이미지를 찾을 수 없습니다"));
        imgRepository.deleteById(imgId);
        amazonS3.deleteObject(bucket, img.getStoredFileName());
    }


    // 전문가 프로필 이미지
    @Transactional
    public String putProfImage(Long expertId, MultipartFile multipartFile) throws IOException {

        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));

        // 아무 파일도 받지 못하면 null을 리턴
        if (multipartFile.isEmpty()) {return null;}

        // 허용된 이미지 확장자 목록
        List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif", "webp");

        // 원본 파일명 추출
        String originalFileName = multipartFile.getOriginalFilename();
        // 파일 확장자 추출
        String fileExtension = extractExt(originalFileName).toLowerCase();

        // 확장자 검사
        if (!allowedExtensions.contains(fileExtension)) {
            throw new IllegalArgumentException("지원하지 않는 파일 형식입니다. 이미지 파일(jpg, jpeg, png, gif, webp)만 업로드 가능합니다.");
        }

        // 이전에 저장된 프로필 사진이 있으면 삭제
        if (expert.getProfileImageUrl() != null){
            deleteProfImage(expertId);
        }

        // 원본 파일명을 서버에 저장된 파일명으로 변경하여 storedFileName에 저장하기 위함 (중복 비허용)
        String storedFileName = "Profile/" + UUID.randomUUID() + "." + extractExt(originalFileName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        // S3에 파일 업로드
        amazonS3.putObject(bucket, storedFileName, multipartFile.getInputStream(), metadata);

        expert.setProfileImageUrl(getFullPath(storedFileName));
        return getFullPath(storedFileName);
    }
//
//    // 프로필 이미지의 S3 전체 주소 조회
//    @Transactional(readOnly = true)
//    public String getProfImage(Long userNo){
//        User user = userRepository.findById(userNo).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다"));
//        if (user.getUserProfImg() == null){
//            return null;
//        }
//        return getFullPath(user.getUserProfImg());
//    }

    // 프로필 이미지 삭제
    @Transactional
    public void deleteProfImage(Long expertId)  {
        Expert expert = expertRepository.findById(expertId).orElseThrow(() -> new ExpertHandler(ErrorStatus.EXPERT_NOT_FOUND));
        amazonS3.deleteObject(bucket, expert.getProfileImageUrl());
        expert.setProfileImageUrl(null);
    }
}
