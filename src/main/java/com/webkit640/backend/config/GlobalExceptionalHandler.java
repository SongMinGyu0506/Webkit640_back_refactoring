package com.webkit640.backend.config;

import com.webkit640.backend.dto.response.ExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionalHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandle(Exception e) {
        ExceptionWrapper response = ExceptionWrapper.builder()
                .description(e.getStackTrace())
                .error(e.toString())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
