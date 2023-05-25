package com.webkit640.backend.controller;

import com.webkit640.backend.config.annotation.Admin;
import com.webkit640.backend.dto.CalendarDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Calendar;
import com.webkit640.backend.service.logic.CalendarService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = {"Content-Disposition"})
@RestController
@RequestMapping("/calendar")
@Slf4j
public class CalendarController {
    private final CalendarService calendarService;

    @Autowired
    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @Admin
    @PostMapping("")
    public ResponseEntity<?> createCalendar(@AuthenticationPrincipal int id, @RequestBody CalendarDto dto) {
        Calendar calendar = calendarService.createCalendar(CalendarDto.toEntity(dto));
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(calendar.getId()).toUri())
                .body(ResponseWrapper.addObject(calendar, HttpStatus.CREATED));
    }

    @GetMapping("")
    public ResponseEntity<?> getCalendars(@AuthenticationPrincipal int id) {
        return ResponseEntity.ok(ResponseWrapper.addObject(calendarService.readCalendars(), HttpStatus.OK));
    }

    @Admin
    @DeleteMapping("/{idCalendar}")
    public ResponseEntity<?> deleteCalendar(@AuthenticationPrincipal int id, @PathVariable int idCalendar) {
        calendarService.deleteCalendar(idCalendar);
        return ResponseEntity.noContent().build();
    }
}
