package com.backend.farmon.service.SearchService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.SearchHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.domain.User;
import com.backend.farmon.repository.CropRepository.CropRepository;
import com.backend.farmon.repository.UserRepository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchCommandServiceImpl implements SearchCommandService {

    private final UserRepository userRepository;
    private final CropRepository cropRepository;
    private final RedisTemplate<String, String> recentSearchLogRedisTemplate;
    private final RedisTemplate<String, String> recommendSearchLogRedisTemplate;
    private final RedisTemplate<String, String> autoSearchLogRedisTemplate;

    private static final int SEARCH_SIZE=10;
    private static final String RECOMMEND_SEARCH_KEY="RecommendSearchLog"; // 추천 검색어 key
    private final String recentSearchKey ="RecentSearchLog"; // 최근 검색어 key
    private final Integer score = 0;
    private final String suffix = "*";    //검색어 자동 완성 기능에서 실제 노출될 수 있는 완벽한 형태의 단어를 구분하기 위한 접미사

    // service Bean이 생성된 이후에 검색어 자동 완성 기능을 위한 데이터들을 Redis에 저장
    @PostConstruct
    public void init() {
        // db에 저장된 모든 작물 카테고리, 이름을 음절 단위로 잘라 모든 Substring을 Redis에 저장해주는 로직
        saveAllSubstring(cropRepository.findAllCropName());
        saveAllSubstring(cropRepository.findAllCropCategory());

        log.info("Redis에 작물 이름, 카테고리 저장 완료");
    }


    // 사용자 최근 검색어 저장
    @Override
    public void saveRecentSearchLog(Long userId, String searchName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if(searchName == null || searchName.isEmpty())
            throw new SearchHandler(ErrorStatus.SEARCH_NOT_EMPTY);

        String key = recentSearchKey + userId;
        log.info("입력한 검색어 key, value: " + key + ", " + searchName);

        // key가 이미 존재하는지 확인
        List<String> searchList = recentSearchLogRedisTemplate.opsForList().range(key, 0, -1);
        if (searchList != null && searchList.contains(searchName)) {
            log.info("exists: true");
            // 이미 존재하는 경우, 해당 검색어를 리스트의 맨 앞으로 이동시켜야 하기 때문에 제거
            recentSearchLogRedisTemplate.opsForList().remove(key, 0, searchName);
        }

        Long size = recentSearchLogRedisTemplate.opsForList().size(key);

        // redis의 현재 크기가 10인 경우
        if (size != null && size == SEARCH_SIZE) {
            // rightPop을 통해 가장 오래된 데이터 삭제
            recentSearchLogRedisTemplate.opsForList().rightPop(key);
        }

        // 새로운 검색어를 추가
        recentSearchLogRedisTemplate.opsForList().leftPush(key, searchName);

//        // 전체 사용자 대상 검색어 추가
//        Double score = recentSearchLogRedisTemplate.opsForZSet().score("RecentSearchLog", searchName);
//        recentSearchLogRedisTemplate.opsForZSet().add("RecentSearchLog", searchName, (score != null) ? score + 1 : 1);

        log.info("최근 검색어 저장 완료 - userId: {}, 검색어: {}", userId, searchName);
    }

    // 검색어와 일치하는 사용자의 최근 검색어 삭제

    @Override
    public void deleteRecentSearchLog(Long userId, String searchName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String key = recentSearchKey + userId;

        Long count = recentSearchLogRedisTemplate.opsForList().remove(key, 1, searchName);
        log.info("삭제된 최근 검색어: {}, 검색 횟수: {}", searchName, count);
    }

    // 사용자 최근 검색어 전체 삭제
    @Override
    public void deleteAllRecentSearchLog(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Set<String> keys = recentSearchLogRedisTemplate.keys(recentSearchKey + userId);

        if (keys != null && !keys.isEmpty()) {
            recentSearchLogRedisTemplate.delete(keys);
        }

        log.info("사용자 최근 검색어 전체 삭제 - userId: {}", userId);
    }

    // 추천 검색어 저장
    @Override
    public void saveRecommendSearchLog(Long userId, String cropName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        if(cropName == null || cropName.isEmpty())
            throw new SearchHandler(ErrorStatus.SEARCH_NOT_EMPTY);

        log.info("입력한 검색어 key, value: " + RECOMMEND_SEARCH_KEY + ", " + cropName);

        // 전체 사용자 대상 검색어 추가
        Double score = recommendSearchLogRedisTemplate.opsForZSet().score(RECOMMEND_SEARCH_KEY, cropName);
        recommendSearchLogRedisTemplate.opsForZSet().add(RECOMMEND_SEARCH_KEY, cropName, (score != null) ? score + 1 : 1);

        log.info("추천 검색어 저장 완료 - userId: {}, 검색어: {}", userId, cropName);
    }

    // 특정 추천 검색어 삭제
    @Override
    public void deleteRecommendSearchLog(Long userId, String cropName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Long count = recommendSearchLogRedisTemplate.opsForZSet().remove(RECOMMEND_SEARCH_KEY, 1, cropName);
        log.info("삭제된 추천 검색어: {}, 검색 횟수: {}", cropName, count);
    }

    // 음절 단위로 쪼개어 자동 완성 검색어 저장
    private void saveAllSubstring(List<String> allDisplayName) {
        // long start2 = System.currentTimeMillis(); //뒤에서 성능 비교를 위해 시간을 재는 용도
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()); //병렬 처리를 위한 스레드풀을 생성하는 과정
        // ExecutorService executorService = Executors.newFixedThreadPool(4); //병렬 처리를 위한 스레드풀을 생성하는 과정

        for (String displayName : allDisplayName) {
            executorService.submit(() -> {  //submit 메서드를 사용해서 병렬 처리할 작업 추가
                //    String threadName = Thread.currentThread().getName();   //멀티 스레드로 병렬 처리가 잘 되고 있는지 확인하기 위해
                //    System.out.println("threadName = " + threadName);   //멀티 스레드로 병렬 처리가 잘 되고 있는지 확인하기 위해
                addToSortedSet(displayName + suffix);

                for (int i = displayName.length(); i > 0; --i) {
                    addToSortedSet(displayName.substring(0, i));
                }
            });
        }
        executorService.shutdown(); //작업이 모두 완료되면 스레드풀을 종료
        // long end2 = System.currentTimeMillis(); //뒤에서 성능 비교를 위해 시간을 재는 용도
        // long elapsed2 = end2 - start2;  //뒤에서 성능 비교를 위해 시간을 재는 용도
    }

    // Redis SortedSet에 추가
    private void addToSortedSet(String value) {
        autoSearchLogRedisTemplate.opsForZSet().add(recentSearchKey, value, score);
    }
}
