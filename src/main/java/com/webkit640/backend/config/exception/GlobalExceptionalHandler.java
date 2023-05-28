package com.webkit640.backend.config.exception;

import com.webkit640.backend.dto.response.ExceptionWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionalHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandle(Exception ex, WebRequest request) {
        ex.printStackTrace();
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(AlreadyExistsException.class)
    public final ResponseEntity<?> handleAlreadyExistsExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.CONFLICT),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(LoginFailedException.class)
    public final ResponseEntity<?> handleLoginFailedExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoAdminException.class)
    public final ResponseEntity<?> handleNoAuthenticationExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.UNAUTHORIZED),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(NotFoundDataException.class)
    public final ResponseEntity<?> handleNotFoundDataExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OAuthLoginException.class)
    public final ResponseEntity<?> handleOAuthLoginExceptions(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.UNAUTHORIZED),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FileServiceException.class)
    public final ResponseEntity<?> handleFileServiceException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.INTERNAL_SERVER_ERROR),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ApplicantLogicException.class)
    public final ResponseEntity<?> handleApplicantLogicException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ExceptionWrapper.makeResponse(ex,request,HttpStatus.FORBIDDEN),HttpStatus.FORBIDDEN);
    }
}
