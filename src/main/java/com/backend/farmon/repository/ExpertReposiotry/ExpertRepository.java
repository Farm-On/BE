package com.backend.farmon.repository.ExpertReposiotry;

import com.backend.farmon.domain.Expert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpertRepository extends JpaRepository<Expert, Long>, ExpertRepositoryCustom {
}
