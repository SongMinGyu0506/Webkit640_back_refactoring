package com.webkit640.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.webkit640.backend.dto.ApplicationDto;
import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.entity.Trainee;
import com.webkit640.backend.repository.*;
import com.webkit640.backend.service.impl.FileEntityServiceImpl;
import com.webkit640.backend.service.logic.ApplicationService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    private String bearerToken;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FileEntityServiceImpl fileEntityService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ApplicantRepository applicantRepository;
    @Autowired
    private FileEntityRepository fileEntityRepository;
    @Autowired
    private TraineeRepository traineeRepository;

    @Autowired
    Gson gson;

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
    String login() throws Exception {
        LoginDtoRequest login = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String loginString = gson.toJson(login);
        MockHttpServletResponse data = mockMvc.perform(post("/auth").contentType(MediaType.APPLICATION_JSON).content(loginString)).andExpect(status().isOk())
                .andReturn().getResponse();
        HashMap<String,List<LinkedTreeMap<String,String>>> jsonMap =  gson.fromJson(data.getContentAsString(), HashMap.class);
        return "Bearer "+jsonMap.get("data").get(0).get("token");
    }


    @Test
    @DisplayName("지원서 입력 테스트")
    @Order(1)
    void apply() throws Exception {
        beforeEach();
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
                        .header("Authorization",bearerToken)
        ).andExpect(status().isCreated());
    }
    @Test
    @Order(2)
    @DisplayName("지원서 ZIP 다운로드 실패 테스트")
    void adminZipDownloadTestFailed() {
        try {
            mockMvc.perform(MockMvcRequestBuilders.get("/application/file")
                    .header("Authorization", login())
            ).andExpect(status().isUnauthorized());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    @Order(3)
    @DisplayName("지원서 ZIP 다운로드 성공 테스트")
    void adminZipDownloadTest() {
        try {
            Member member = memberRepository.findByEmail("test@test.com");
            member.setAdmin(true);
            memberRepository.save(member);

            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/application/file")
                    .header("Authorization", login())
            ).andExpect(status().isOk()).andReturn();
            assertAll(
                    ()->assertThat(result.getResponse().getContentType()).isEqualTo(MediaType.APPLICATION_OCTET_STREAM_VALUE),
                    ()->assertThat(result.getResponse().getHeader(HttpHeaders.CONTENT_DISPOSITION)).contains("attachment")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    @Order(4)
    @DisplayName("관리자 지원자 선택 테스트")
    void adminSelection() throws Exception {
        /**
         * ADMIN 권한 부여
         */
        Member member = memberRepository.findByEmail("test@test.com");
        member.setAdmin(true);
        memberRepository.save(member);

        /**
         * dto 생성
         */
        List<String> emails = new ArrayList<>();
        emails.add("test@test.com");
        ApplicationDto.SelectionRequestDto dto = ApplicationDto.SelectionRequestDto.builder().emails(emails).build();

        /**
         * 테스트 수행, 정상 수행 결과 : 204(noContent), applicant.isAdminApply == true
         */
        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection")
                .header("Authorization",login())
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(dto))).andExpect(status().isNoContent());

        Applicant applicant = applicantRepository.findByMemberId(member.getId());
        assertAll(
                ()->assertThat(applicant.isAdminApply()).isEqualTo(true)
        );
    }

    @Test
    @DisplayName("지원자 교육 최종결정 테스트")
    @Order(5)
    @Transactional
    void selectionConfirmation() throws Exception {
        Member member = memberRepository.findByEmail("test@test.com");
        member.setAdmin(true);
        memberRepository.save(member);

        mockMvc.perform(MockMvcRequestBuilders.patch("/application/selection/confirmation").header("Authorization",login()))
                .andExpect(status().isNoContent());
        mockMvc.perform(post("/trainee").header("Authorization", login()))
                .andExpect(status().isCreated()).andReturn();

        Applicant applicant = traineeRepository.findById(1).getApplicant();
        assertAll(
                ()->assertThat(applicant.getName()).isEqualTo("song"),
                ()->assertThat(applicant.getMajor()).isEqualTo("com"),
                ()->assertThat(applicant.getSchool()).isEqualTo("string")
        );
    }
    @Test
    @DisplayName("지원자 목록 출력")
    @Order(6)
    void viewList() throws Exception {
        Member member = memberRepository.findByEmail("test@test.com");
        member.setAdmin(true);
        memberRepository.save(member);

        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();
        params.add("year","2023");
        params.add("school","string");
        params.add("major","com");

        MvcResult result1 = mockMvc.perform(MockMvcRequestBuilders.get("/application").header("Authorization", login()).params(params))
                .andExpect(status().isOk()).andReturn();
        MvcResult result2 = mockMvc.perform(MockMvcRequestBuilders.get("/application").header("Authorization", login()))
                .andExpect(status().isOk()).andReturn();
        params.remove("year");
        MvcResult result3 = mockMvc.perform(MockMvcRequestBuilders.get("/application").header("Authorization", login()).params(params))
                .andExpect(status().isOk()).andReturn();
        params.remove("school");
        MvcResult result4 = mockMvc.perform(MockMvcRequestBuilders.get("/application").header("Authorization", login()).params(params))
                .andExpect(status().isOk()).andReturn();
        System.out.println(result1.getResponse().getContentAsString());
    }

    @Test
    @DisplayName("지원자 검색")
    @Order(7)
    void viewApplicant() throws Exception {
        Member member = memberRepository.findByEmail("test@test.com");
        member.setAdmin(true);
        memberRepository.save(member);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/application/test@test.com")
                .header("Authorization",login())).andExpect(status().isOk()).andReturn();

        System.out.println(result.getResponse().getContentAsString());

    }
}