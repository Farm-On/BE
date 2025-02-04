package com.backend.farmon.service.SearchService;

import com.backend.farmon.converter.ConvertTime;
import com.backend.farmon.dto.home.HomeResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class SearchQueryServiceImpl implements SearchQueryService{
    private final RedisTemplate<String, String> recentSearchLogRedisTemplate;
    private final RedisTemplate<String, String> recommendSearchLogRedisTemplate;

    private static final String RECOMMEND_SEARCH_KEY="RecommendSearchLog"; // 추천 검색어 key

    // 사용자 최근 검색어 리스트 조회
    @Override
    public HomeResponse.RecentSearchListDTO findRecentSearchLogs(Long userId) {
        return HomeResponse.RecentSearchListDTO.builder()
                .recentSearchList( recentSearchLogRedisTemplate.opsForList().range("RecentSearchLog"+userId, 0, 9))
                .build();
    }

    // 추천 검색어 스케줄링
    @Override
    public HomeResponse.RecommendSearchListDTO getRecommendSearchNameRank(){
        return HomeResponse.RecommendSearchListDTO.builder()
                .recommendSearchList(recommendSearchNameRankList())
                .build();
    }

    // 추천 검색어 리스트 조회
     List<String> recommendSearchNameRankList(){
        ZSetOperations<String, String> zSetOperations = recommendSearchLogRedisTemplate.opsForZSet();

        // score 순으로 10개 보여줌 (value, score) 형태
        Set<ZSetOperations.TypedTuple<String>> typedTuples = zSetOperations.reverseRangeWithScores(RECOMMEND_SEARCH_KEY, 0, 9);

        // 검색어만 추출하여 리스트로 변환
        if (typedTuples != null) {
            return typedTuples.stream()
                    .map(ZSetOperations.TypedTuple::getValue)
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
