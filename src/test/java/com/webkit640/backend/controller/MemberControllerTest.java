package com.webkit640.backend.controller;

import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.repository.MemberRepository;
import com.webkit640.backend.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @AfterEach
    void init() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("로컬 회원가입 테스트")
    void signUp() {
        String name = "test_name";
        String email = "test@test.com";
        String password = "1234";
        String memberBelong = "Student";
        String memberType = "ADMIN";

        SignupDtoRequest dto = SignupDtoRequest.builder()
                .name("test_name")
                .email("test@test.com")
                .password("1234")
                .memberBelong("Student")
                .memberType("ADMIN")
                .build();
        String url = "http://localhost:" + port + "/auth/sign-up";

        ResponseEntity<ResponseWrapper> response = restTemplate.postForEntity(url,dto,ResponseWrapper.class);
//        LinkedHashMap<String,Object> lhm = (LinkedHashMap<String, Object>) response.getBody().getData().get(0);
//
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
//        assertThat(lhm.get("name")).isEqualTo("test_name");
//        assertThat(lhm.get("email")).isEqualTo("test@test.com");
//        assertThat(lhm.get("memberBelong")).isEqualTo("Student");
//        assertThat(lhm.get("memberType")).isEqualTo("ADMIN");
    }
    
    @Test
    @DisplayName("로컬 로그인 테스트")
    void login() {
        SignupDtoRequest dto = SignupDtoRequest.builder()
                .name("test_name")
                .email("test@test.com")
                .password("1234")
                .memberBelong("Student")
                .memberType("ADMIN")
                .build();

        String url = "http://localhost:" + port + "/auth/sign-up";
        ResponseEntity<ResponseWrapper> response = restTemplate.postForEntity(url,dto,ResponseWrapper.class);

        LoginDtoRequest dtos = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String urls = "http://localhost:"+port+"/auth/login";
        response = restTemplate.postForEntity(urls,dtos,ResponseWrapper.class);
        LinkedHashMap<String,Object> lhm = (LinkedHashMap<String, Object>) response.getBody().getData().get(0);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(lhm.get("email")).isEqualTo("test@test.com");
        assertThat(lhm.get("memberBelong")).isEqualTo("Student");
        assertThat(lhm.get("memberType")).isEqualTo("ADMIN");
        System.out.println(lhm.get("token"));
    }

    @Test
    @DisplayName("회원 목록 열람")
    void viewMembers() {
        SignupDtoRequest dto = SignupDtoRequest.builder()
                .name("test_name")
                .email("test@test.com")
                .password("1234")
                .memberBelong("Student")
                .memberType("ADMIN")
                .build();

        String url = "http://localhost:" + port + "/auth/sign-up";
        ResponseEntity<ResponseWrapper> response = restTemplate.postForEntity(url,dto,ResponseWrapper.class);

        LoginDtoRequest dtos = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String urls = "http://localhost:"+port+"/auth/login";
        response = restTemplate.postForEntity(urls,dtos,ResponseWrapper.class);
        LinkedHashMap<String,Object> lhm = (LinkedHashMap<String, Object>) response.getBody().getData().get(0);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+lhm.get("token"));
        HttpEntity request = new HttpEntity(headers);
        String requestUrl = "http://localhost:"+port+"/auth/view-members";
        response = restTemplate.exchange(requestUrl,HttpMethod.GET,request,ResponseWrapper.class);
        List<LinkedHashMap<String,Object>> list = (List<LinkedHashMap<String, Object>>) response.getBody().getData().get(0);
        System.out.println(list.get(0));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(list.get(0).get("email")).isEqualTo("test@test.com");
        assertThat(list.get(0).get("memberBelong")).isEqualTo("Student");
        assertThat(list.get(0).get("memberType")).isEqualTo("ADMIN");
        assertThat(list.get(0).get("name")).isEqualTo("test_name");
        assertThat(list.get(0).get("admin")).isEqualTo(false);
    }

}