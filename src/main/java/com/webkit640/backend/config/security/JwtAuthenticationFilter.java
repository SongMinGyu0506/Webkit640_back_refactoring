package com.webkit640.backend.config.security;

import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;

@Component
public interface JwtAuthenticationFilter {
    String parseBearerToken(HttpServletRequest request);
}
