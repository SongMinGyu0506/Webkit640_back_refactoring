package com.webkit640.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit640.backend.dto.CalendarDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Commit
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CalendarControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAccount("test@test.com")
    @DisplayName("일정 생성 테스트")
    void createCalendar() throws Exception {
        String dto = objectMapper.writeValueAsString(CalendarDto.builder().endDate("2020-01-01").startDate("2020-01-01").title("test").build());
        mockMvc.perform(MockMvcRequestBuilders.post("/calendar").content(dto).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("일정 조회 테스트")
    void getCalendars() throws Exception {
        createCalendar();
        createCalendar();

        mockMvc.perform(MockMvcRequestBuilders.get("/calendar").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("일정 삭제 테스트")
    void deleteCalendar() throws Exception {
        createCalendar();
        mockMvc.perform(MockMvcRequestBuilders.delete("/calendar/1")).andExpect(status().isNoContent()).andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.get("/calendar").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andDo(print());
    }
}