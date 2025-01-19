package com.backend.farmon.reposiotry.UserRepository;

import com.backend.farmon.domain.Expert;
import com.backend.farmon.domain.User;
import com.backend.farmon.reposiotry.ExpertReposiotry.ExpertRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
}
