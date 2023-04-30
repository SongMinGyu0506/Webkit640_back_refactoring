package com.webkit640.backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionWrapper {
    private HttpStatus status;
    private String error;
    private StackTraceElement[] description;
}
