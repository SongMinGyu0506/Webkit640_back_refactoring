package com.webkit640.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Not found data")
public class NotFoundDataException extends RuntimeException{
    public NotFoundDataException(String message) {
        super(message);
    }
}
