package com.webkit640.backend.config.exception;



import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Applicant Logic Exception")
public class ApplicantLogicException extends RuntimeException{
    public ApplicantLogicException(String message) {
        super(message);
    }
}
