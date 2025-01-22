package com.backend.farmon.config.security;

import com.backend.farmon.repository.UserRepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserAuthorizationUtil {

    private final UserRepository userRepository;

    // 현재 로그인한 유저의 userId 반환해주는 메서드
    public Long getCurrentUserId() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication or principal is null");
        }
        Long userId = (Long) authentication.getPrincipal();
        return userId;
    }

    // 현재 로그인한 유저의 role 반환해주는 메서드
    public String getCurrentUserRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new AuthenticationCredentialsNotFoundException("Authentication or principal is null");
        }
        GrantedAuthority authority = authentication.getAuthorities().stream()
                .findFirst()
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("No roles found for the authenticated user"));

        return authority.getAuthority();
    }

}
