package com.backend.farmon.repository.SmsAuthRepository;

import com.backend.farmon.domain.SmsAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SmsAuthRepository extends JpaRepository<SmsAuth, Long> {
    Optional<SmsAuth> findByPhoneNum(String phoneNum);
}