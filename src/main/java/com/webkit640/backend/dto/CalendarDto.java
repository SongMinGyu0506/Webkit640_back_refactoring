package com.webkit640.backend.dto;

import com.webkit640.backend.entity.Calendar;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalendarDto {
    private int id;
    private String title;
    private String startDate;
    private String endDate;

    public static Calendar toEntity(CalendarDto dto) {
        return Calendar.builder()
              .id(dto.getId())
              .title(dto.getTitle())
              .startDate(dto.getStartDate())
              .endDate(dto.getEndDate())
              .build();
    }
    public static CalendarDto toDto(Calendar entity) {
        return CalendarDto.builder()
              .id(entity.getId())
              .title(entity.getTitle())
              .startDate(entity.getStartDate())
              .endDate(entity.getEndDate())
              .build();
    }
}
