package com.backend.farmon.repository.UserRepository;

import com.backend.farmon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
