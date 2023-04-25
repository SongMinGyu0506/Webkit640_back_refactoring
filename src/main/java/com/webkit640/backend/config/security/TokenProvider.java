package com.webkit640.backend.config.security;

import com.webkit640.backend.entity.Member;
import org.springframework.stereotype.Component;

@Component
public interface TokenProvider {
    int validateAndGetUserId(String token);
    String create(Member member);
}
