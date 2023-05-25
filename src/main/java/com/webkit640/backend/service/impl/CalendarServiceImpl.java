package com.webkit640.backend.service.impl;

import com.webkit640.backend.entity.Calendar;
import com.webkit640.backend.repository.repository.CalendarRepository;
import com.webkit640.backend.service.logic.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CalendarServiceImpl implements CalendarService {
    private final CalendarRepository calendarRepository;

    @Autowired
    public CalendarServiceImpl(CalendarRepository calendarRepository) {
        this.calendarRepository = calendarRepository;
    }

    @Override
    public Calendar createCalendar(Calendar calendar) {
        return calendarRepository.save(calendar);
    }

    @Override
    public List<Calendar> readCalendars() {
        return calendarRepository.findAll();
    }

    @Override
    public void deleteCalendar(int calendarId) {
        calendarRepository.deleteById(calendarId);
    }
}
