package com.webkit640.backend.service;

import org.springframework.stereotype.Service;

@Service
public interface OAuthLoginService {
    String getAccessToken(String code);
    String accessUser(String token);
}
