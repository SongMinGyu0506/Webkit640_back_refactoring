package com.webkit640.backend.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Login Failed")
public class FileServiceException extends RuntimeException{
    public FileServiceException(String message) {
        super(message);
    }
}
