package com.webkit640.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Commit
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MemberControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void signup() throws Exception {
        String value = objectMapper.writeValueAsString(SignupDtoRequest.builder()
                .email("ttt@ttt.com")
                .password("1234")
                .memberBelong("test")
                .memberType("test")
                .name("test").build());
        mvc.perform(MockMvcRequestBuilders.post("/auth/member")
                .contentType("application/json")
                .content(value)).andExpect(status().isCreated()).andDo(print());

    }

    @Test
    void login() throws Exception {
        signup();
        String value = objectMapper.writeValueAsString(
                LoginDtoRequest.builder().email("ttt@ttt.com").password("1234").build());

        mvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType("application/json")
                .content(value)).andExpect(status().isOk()).andDo(print());

    }

    @Test
    @WithAccount("test@test.com")
    void viewMembers() throws Exception {
        signup();
        mvc.perform(MockMvcRequestBuilders.get("/auth/member")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    void viewMember() throws Exception {
        signup();
        mvc.perform(MockMvcRequestBuilders.get("/auth/member/2")).andExpect(status().isOk()).andDo(print());
    }
}
