package com.backend.farmon.controller;

import com.backend.farmon.dto.home.HomeResponse;
import com.backend.farmon.service.SearchService.SearchCommandService;
import com.backend.farmon.service.SearchService.SearchQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class TestController {

    private final RedisTemplate<String, String> stringRedisTemplate;
    private final SearchCommandService searchCommandService;
    private final SearchQueryService searchQueryService;

    @GetMapping("/test")
    public String test(){
        return "Hello World!";
    }

    @GetMapping("/test-redis/set")
    public String testRedis(@RequestParam String key, @RequestParam String value) {
        try {
            // Redis에 데이터 저장
            stringRedisTemplate.opsForValue().set(key, value);

            // Redis에서 데이터 가져오기
            String redisValue = stringRedisTemplate.opsForValue().get(key);

            return redisValue != null ? "Redis 연결 성공! Value: " + redisValue : "Redis 연결 실패!";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Redis 오류: " + e.getMessage();
        }
    }

    @GetMapping("/test-redis/exist")
    public String checkKey(@RequestParam String key) {
        try {
            // Redis에 해당 키가 존재하는지 확인
            Boolean exists = stringRedisTemplate.hasKey(key);

            // 결과 반환
            if (exists != null && exists) {
                return "키가 Redis에 존재합니다: " + key;
            } else {
                return "키가 Redis에 존재하지 않습니다: " + key;
            }
        } catch (Exception e) {
            log.error("Redis 오류: ", e);
            return "Redis 오류: " + e.getMessage();
        }
    }

    @GetMapping("/test-redis/set/recommend")
    public  HomeResponse.RecommendSearchListDTO testRecommendRedis(@RequestParam Long userId, @RequestParam String value) {
        // Redis에 데이터 저장
        searchCommandService.saveRecommendSearchLog(userId, value);

        return searchQueryService.getRecommendSearchNameRank();
    }
}