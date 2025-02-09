package com.backend.farmon.repository.PortfolioRepository;

import com.backend.farmon.domain.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
