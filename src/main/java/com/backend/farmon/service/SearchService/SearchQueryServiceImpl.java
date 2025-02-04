package com.backend.farmon.service.SearchService;

import com.backend.farmon.converter.ConvertTime;
import com.backend.farmon.domain.Crop;
import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.repository.CropRepository.CropRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchQueryServiceImpl implements SearchQueryService{
    private final CropRepository cropRepository;

    private final RedisTemplate<String, String> recentSearchLogRedisTemplate;
    private final RedisTemplate<String, String> recommendSearchLogRedisTemplate;
    private final RedisTemplate<String, String> autoSearchLogRedisTemplate;
    private final String suffix = "*";    //검색어 자동 완성 기능에서 실제 노출될 수 있는 완벽한 형태의 단어를 구분하기 위한 접미사
    private Integer maxSize = 10;    //검색어 자동 완성 기능 최대 개수

    private static final String RECOMMEND_SEARCH_KEY="RecommendSearchLog"; // 추천 검색어 key
    private final String recentSearchKey="RecentSearchLog"; // 최근 검색어 key

    // 사용자 최근 검색어 리스트 조회
    @Override
    public HomeResponse.RecentSearchListDTO findRecentSearchLogs(Long userId) {
        return HomeResponse.RecentSearchListDTO.builder()
                .recentSearchList( recentSearchLogRedisTemplate.opsForList().range(recentSearchKey+userId, 0, 9))
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

    // 자동 완성 검색어 조회
    @Override
    public List<String> autoSearchNameList(String keyword) {
        Long index = findFromSortedSet(keyword);  // 사용자가 입력한 검색어를 바탕으로 Redis에서 조회한 결과 매칭되는 index

        if (index == null) {
            // 만약 사용자 검색어 바탕으로 자동 완성 검색어를 만들 수 없으면 추천 검색어 리스트 반환
            return recommendSearchNameRankList();
        }

        Set<String> allValuesAfterIndexFromSortedSet = findAllValuesAfterIndexFromSortedSet(index);   // 사용자 검색어 이후로 정렬된 Redis 데이터들 가져오기

        // 자동 완성 검색어 리스트 생성 (키워드가 단어 어디에 있든 포함되도록 수정)
        List<String> autoCorrectKeywordList = allValuesAfterIndexFromSortedSet.stream()
                .filter(value -> value.contains(keyword) && value.endsWith(suffix)) // startsWith -> contains 변경
                .map(value -> StringUtils.removeEnd(value, suffix))
                .limit(maxSize)
                .collect(Collectors.toList());

        // 필터링된 값들을 기반으로 작물 카테고리 검색
        Set<String> categorySet = new HashSet<>(autoCorrectKeywordList);
        // 카테고리에 해당하는 모든 작물 이름 가져오기
        List<String> cropNames = cropRepository.findCropNamesByCategories(categorySet);
        // 최종 자동 완성 리스트 생성
        autoCorrectKeywordList.addAll(cropNames);

        return autoCorrectKeywordList;
    }

    // Redis SortedSet에서 Value를 찾아 인덱스를 반환
    private Long findFromSortedSet(String value) {
        return autoSearchLogRedisTemplate.opsForZSet().rank(recentSearchKey, value);
    }

    private Set<String> findAllValuesAfterIndexFromSortedSet(Long index) {
        return autoSearchLogRedisTemplate.opsForZSet().range(recentSearchKey, index, index + 7);
    }
}
