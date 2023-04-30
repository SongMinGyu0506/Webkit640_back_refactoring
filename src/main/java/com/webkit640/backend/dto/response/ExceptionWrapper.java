package com.webkit640.backend.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExceptionWrapper {
    private Date timestamp;
    private HttpStatus status;
    private String error;
    private String description;


    public static ExceptionWrapper makeResponse(Exception ex, WebRequest request) {
        return ExceptionWrapper.builder()
                .description(request.getDescription(false))
                .error(ex.getMessage())
                .timestamp(new Date())
                .status(HttpStatus.BAD_REQUEST)
                .build();
    }
}
