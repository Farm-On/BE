package com.backend.farmon.service.SearchService;

import com.backend.farmon.apiPayload.code.status.ErrorStatus;
import com.backend.farmon.apiPayload.exception.handler.SearchHandler;
import com.backend.farmon.apiPayload.exception.handler.UserHandler;
import com.backend.farmon.domain.User;
import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SearchCommandServiceImpl implements SearchCommandService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, String> recentSearchLogRedisTemplate;
    private final RedisTemplate<String, String> recommendSearchLogRedisTemplate;

    private static final int SEARCH_SIZE=10;
    private static final String RECOMMEND_SEARCH_KEY="RecommendSearchLog"; // 추천 검색어 key
    private final String recentSearchKey ="RecentSearchLog"; // 최근 검색어 key

    // 사용자 최근 검색어 저장
    @Transactional
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
    @Transactional
    @Override
    public void deleteRecentSearchLog(Long userId, String searchName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        String key = recentSearchKey + userId;

        Long count = recentSearchLogRedisTemplate.opsForList().remove(key, 1, searchName);
        log.info("삭제된 최근 검색어: {}, 검색 횟수: {}", searchName, count);
    }

    // 사용자 최근 검색어 전체 삭제
    @Transactional
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
    @Transactional
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
    @Transactional
    @Override
    public void deleteRecommendSearchLog(Long userId, String cropName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));

        Long count = recommendSearchLogRedisTemplate.opsForList().remove(RECOMMEND_SEARCH_KEY, 1, cropName);
        log.info("삭제된 추천 검색어: {}, 검색 횟수: {}", cropName, count);
    }
}
