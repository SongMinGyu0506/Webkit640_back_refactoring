package com.webkit640.backend.dto.response;


import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseWrapper<T> {
    private LocalDate time;
    private HttpStatus status;
    private List<T> data;


//    public ResponseWrapper addObject(T obj) {
//        List<T> dataList = new ArrayList<>();
//        dataList.add(obj);
//        return ResponseWrapper.<T>builder()
//                .status(HttpStatus.OK)
//                .data(dataList)
//                .time(LocalDate.now())
//                .build();
//    }

    public static <T> ResponseWrapper addObject(T obj,HttpStatus status) {
        List<T> dataList = new ArrayList<>();
        dataList.add(obj);
        return ResponseWrapper.<T>builder()
                .status(status)
                .data(dataList)
                .time(LocalDate.now())
                .build();
    }
}
