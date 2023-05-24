package com.webkit640.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "user error")
public class NoAuthenticationUserException extends RuntimeException{
    public NoAuthenticationUserException(String message) {
        super(message);
    }
}
