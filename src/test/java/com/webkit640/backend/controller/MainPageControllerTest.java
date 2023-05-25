package com.webkit640.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit640.backend.dto.MainPageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Commit
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MainPageControllerTest {
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithAccount("test@test.com")
    @DisplayName("메인화면 열람 테스트")
    void getMainPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/main-page")).andExpect(status().isBadRequest()).andDo(print());
        addMainPage();
        mockMvc.perform(MockMvcRequestBuilders.get("/main-page")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("메인화면 추가 테스트")
    void addMainPage() throws Exception {
        String dto = """
                {
                    "recruitmentDate":"test1",
                    "recruitmentTarget": "test2",
                    "totalRecruitment":"test3",
                    "eligibility":"test4",
                    "documentSubmissionPeriod":"test5",
                    "additionalRecruitmentPeriod":"test6",
                    "passAnnouncementDate":"test7",
                    "trainingStartDate":"test8",
                    "cumulativeStudents":"test9",
                    "completeCardinalNumber":"test10",
                    "nonMajor":"test11"
                }""";
        mockMvc.perform(MockMvcRequestBuilders.post("/main-page").contentType("application/json").content(dto)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("관리자 권한 변경 테스트")
    void updateMainPage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/main-page/admin-change?memberId=1")).andExpect(status().isNoContent()).andDo(print());
    }
}