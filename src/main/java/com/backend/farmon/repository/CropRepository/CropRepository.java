package com.backend.farmon.repository.CropRepository;

import com.backend.farmon.domain.Area;
import com.backend.farmon.domain.Crop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CropRepository extends JpaRepository<Crop, Long> {
    Optional<Crop> findByName(String name);

    // 중복을 제외한 모든 작물 카테고리 반환
    @Query("SELECT DISTINCT c.category FROM Crop c")
    List<String> findAllCropCategory();

    // 모든 작물 이름 반환
    @Query("SELECT c.name FROM Crop c")
    List<String> findAllCropName();

    // 카테고리와 일치하는 작물 반환
    List<Crop> findByCategory(String category);

    // 카테고리에 해당하는 모든 작물 이름 반환
    @Query("SELECT c.name FROM Crop c WHERE c.category IN :categories")
    List<String> findCropNamesByCategories(@Param("categories") Set<String> categories);
}
