package com.webkit640.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.webkit640.backend.dto.ApplicationDto;
import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.LoginDtoResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String bearerToken;

    @Autowired
    Gson gson;

    @BeforeEach
    void beforeEach() throws Exception {
        SignupDtoRequest signupDtoRequest = SignupDtoRequest.builder()
                .memberType("student")
                .email("test@test.com")
                .memberBelong("Kumoh")
                .name("test")
                .password("1234")
                .build();
        String json = gson.toJson(signupDtoRequest);
        mockMvc.perform(post("/auth/member").contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated());
        LoginDtoRequest login = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String loginString = gson.toJson(login);
        MockHttpServletResponse data = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(loginString)).andExpect(status().isOk())
                .andReturn().getResponse();
        HashMap<String,List<LinkedTreeMap<String,String>>> jsonMap =  gson.fromJson(data.getContentAsString(), HashMap.class);
        bearerToken = "Bearer "+jsonMap.get("data").get(0).get("token");
    }

    @Test
    @DisplayName("지원서 입력 테스트")
    void apply() throws Exception {
        Map<String,String> map = new HashMap<>();
        map.put("name","song");
        map.put("major","com");
        map.put("school","string");
        map.put("schoolNumber","0000");
        map.put("application","asdf");
        map.put("schoolYear","1234");
        String contents = objectMapper.writeValueAsString(map);

        MockMultipartFile file = new MockMultipartFile(
                "multipartFile",
                "test.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                "Hello, World!".getBytes()
        );
        MockMultipartFile dto = new MockMultipartFile(
                "dto",
                "dto",
                "application/json",
                contents.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(
                MockMvcRequestBuilders.multipart("/application")
                        .file(file)
                        .file(dto)
                        .header("Authorization",bearerToken)
        ).andExpect(status().isCreated());
    }
    @Test
    void other() throws IOException {
        Path path = new File("C:\\Users\\song\\Documents\\Webkit640uploadTest\\2023-1\\a.hwp").toPath();
        String mimeType = Files.probeContentType(path);
        System.out.println(mimeType);
    }
}