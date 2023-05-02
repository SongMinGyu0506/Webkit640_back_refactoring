package com.webkit640.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "OAuth login error")
public class OAuthLoginException extends RuntimeException{
    public OAuthLoginException(String message) {
        super(message);
    }
}
