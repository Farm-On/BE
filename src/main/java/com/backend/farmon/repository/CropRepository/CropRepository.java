package com.backend.farmon.repository.CropRepository;

import com.backend.farmon.domain.Crop;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CropRepository extends JpaRepository<Crop, Long> {
}
