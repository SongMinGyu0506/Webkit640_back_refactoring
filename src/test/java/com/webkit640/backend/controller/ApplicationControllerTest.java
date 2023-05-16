package com.webkit640.backend.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.webkit640.backend.config.security.TokenProvider;
import com.webkit640.backend.dto.ApplicationDto;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.entity.Trainee;
import com.webkit640.backend.repository.*;
import com.webkit640.backend.service.logic.MemberService;
import com.webkit640.backend.service.logic.TraineeService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Commit
public class ApplicationControllerTest {

    @Autowired
    TraineeRepository traineeRepository;
    @Autowired
    Gson gson;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    MemberService memberService;

    ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    FileEntityRepository fileEntityRepository;
    @Autowired
    ApplicantRepository applicantRepository;
    @Autowired
    TraineeService traineeService;

    @AfterEach
    void afterEach() {
        traineeRepository.deleteAll();
        applicantRepository.deleteAll();
        fileEntityRepository.deleteAll();
        memberRepository.deleteAll();
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("지원서 입력 테스트")
    void others() throws Exception {
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
                MockMvcRequestBuilders.multipart("/application/submit")
                        .file(file)
                        .file(dto)
        ).andExpect(status().isCreated());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("지원서 개별 다운로드 성공 테스트")
    void successDownloadApplication() throws Exception {
        others();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/application/file/test@test.com")).
                andExpect(status().isOk()).
                andDo(print()).
                andReturn();

        assertAll(
                ()->assertThat(result.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM_VALUE),
                ()->assertThat(result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment")
        );
    }
    @Test
    @WithAccount("test@test.com")
    @DisplayName("지원서 개별 다운로드 실패 테스트")
    void failedDownloadApplication() throws Exception {
        others();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/application/file/test12343@test.com")).
                andExpect(status().isInternalServerError()).
                andDo(print()).
                andReturn();

        assertAll(
                ()->assertThat(result.getResponse().getContentType()).isEqualTo("application/json"),
                ()->assertThat(result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains("f.txt")
        );
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("지원서 ZIP 다운로드 성공 테스트")
    void test2() {
        try {
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/application/file")
            ).andExpect(status().isOk()).andDo(print()).andReturn();
            assertAll(
                    ()->assertThat(result.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM_VALUE),
                    ()->assertThat(result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("지원서 ZIP 다운로드 실패 테스트")
    void test3() {
        try {
            Member member = memberRepository.findByEmail("test@test.com");
            member.setAdmin(false);
            memberRepository.save(member);

            mockMvc.perform(MockMvcRequestBuilders.get("/application/file")).andExpect(status().isUnauthorized()).andDo(print());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("관리자 지원자 선택 테스트")
    void test4() throws Exception {
        others();
        List<String> emails = new ArrayList<>();
        emails.add("test@test.com");
        ApplicationDto.SelectionRequestDto dto = ApplicationDto.SelectionRequestDto.builder().emails(emails).build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(dto))).andDo(print()).andExpect(status().isNoContent());

        Applicant applicant = memberRepository.findByEmail("test@test.com").getApplicant();
        assertAll(
                ()->assertThat(applicant.isAdminApply()).isEqualTo(true)
        );
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("관리자 지원자 선택 실패(해당이메일 없음) 테스트")
    void test4Failed1() throws Exception {
        others();
        List<String> emails = new ArrayList<>();
        emails.add("test1234@test.com");
        ApplicationDto.SelectionRequestDto dto = ApplicationDto.SelectionRequestDto.builder().emails(emails).build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(dto))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("관리자 지원자 선택 실패(Null List) 테스트")
    void test4Failed2() throws Exception {
        others();
        List<String> emails = null;
        ApplicationDto.SelectionRequestDto dto = ApplicationDto.SelectionRequestDto.builder().emails(emails).build();

        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(dto))).andDo(print()).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("지원자 교육 최종결정 테스트")
    @Transactional
    @WithAccount("test@test.com")
    void selectionConfirmation() throws Exception {
        test4();
        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection/confirmation?email=test@test.com"))
                .andExpect(status().isNoContent()).andDo(print());
        mockMvc.perform(post("/trainee"))
                .andExpect(status().isCreated()).andDo(print()).andReturn();

        //Applicant applicant = traineeRepository.findById(1).getApplicant();
        Applicant applicant = traineeRepository.findAll().get(0).getApplicant();
        assertAll(
                ()->assertThat(applicant.getName()).isEqualTo("song"),
                ()->assertThat(applicant.getMajor()).isEqualTo("com"),
                ()->assertThat(applicant.getSchool()).isEqualTo("string")
        );
    }
    @Test
    @DisplayName("지원자 교육 최종결정 실패(미등록 사용자) 테스트")
    @Transactional
    @WithAccount("test@test.com")
    void selectionConfirmationFailed() throws Exception {
        test4();
        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection/confirmation?email=test1234@test.com"))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("지원자 교육 최종결정 실패(미등록 지원자) 테스트")
    @Transactional
    @WithAccount("test@test.com")
    void selectionConfirmationFailed2() throws Exception {
        //test4();
        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection/confirmation?email=test@test.com"))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    @DisplayName("지원자 목록 출력 테스트")
    @WithAccount("test@test.com")
    void viewList() throws Exception {
        others();
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("year","2023");
        params.add("school","string");
        params.add("major","com");

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/application").params(params))
                .andExpect(status().isOk()).andDo(print()).andReturn();
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/application"))
                .andExpect(status().isOk()).andReturn();
        params.remove("year");
        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.get("/application"))
                .andExpect(status().isOk()).andReturn();
        params.remove("school");
        MvcResult result4 = mockMvc.perform(MockMvcRequestBuilders.get("/application").params(params))
                .andExpect(status().isOk()).andReturn();
        System.out.println(result1.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("지원자 목록 출력 실패 테스트")
    void viewListFailed() throws Exception {
        //others();
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("year","2023");
        params.add("school","string");
        params.add("major","com");

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/application").params(params))
                .andExpect(status().isInternalServerError()).andDo(print()).andReturn();
    }

    @Test
    @DisplayName("지원자 검색")
    @WithAccount("test@test.com")
    void viewApplicant() throws Exception {
        others();

        mockMvc.perform(
                MockMvcRequestBuilders.get("/application/test@test.com")).
                andExpect(status().isOk()).andDo(print());
    }

    @Test
    @DisplayName("교육생 목록")
    @WithAccount("test@test.com")
    @Transactional
    void viewTrainees() throws Exception {
        selectionConfirmation();
        //List<Trainee> trainees = traineeService.searchTrainee(null, null, null);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/trainee")
        ).andExpect(status().isOk()).andDo(print());
    }

}
