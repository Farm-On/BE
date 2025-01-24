package com.backend.farmon.repository.PostRepository;

import com.backend.farmon.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {

}
