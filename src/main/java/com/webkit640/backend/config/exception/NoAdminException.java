package com.webkit640.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Unauthorized user")
public class NoAdminException extends RuntimeException {
    public NoAdminException(String message) {
        super(message);
    }
}
