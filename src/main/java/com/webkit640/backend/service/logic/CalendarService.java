package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Calendar;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CalendarService {
    Calendar createCalendar(Calendar calendar);
    List<Calendar> readCalendars();
    void deleteCalendar(int calendarId);
}
