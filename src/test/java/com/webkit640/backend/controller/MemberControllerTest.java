package com.webkit640.backend.controller;

import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.AllMemberDtoResponse;
import com.webkit640.backend.dto.response.LoginDtoResponse;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.MemberRepository;
import com.webkit640.backend.service.MemberService;
import org.aspectj.lang.annotation.Before;
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
        ResponseEntity<SignupDtoRequest> response = restTemplate.postForEntity(url,dto,SignupDtoRequest.class);
        SignupDtoRequest result = response.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert result != null;
        assertThat(result.getEmail()).isEqualTo("test@test.com");
        assertThat(result.getMemberType()).isEqualTo("ADMIN");
    }
    
    @Test
    @DisplayName("로컬 로그인 테스트")
    void login() {
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
        ResponseEntity<SignupDtoRequest> response = restTemplate.postForEntity(url,dto,SignupDtoRequest.class);

        LoginDtoRequest dtos = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String urls = "http://localhost:"+port+"/auth/login";
        ResponseEntity<LoginDtoResponse> responses = restTemplate.postForEntity(urls,dtos,LoginDtoResponse.class);
        LoginDtoResponse body = responses.getBody();

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assert body != null;
        assertThat(body.getEmail()).isEqualTo("test@test.com");
        assertThat(body.getMemberBelong()).isEqualTo("Student");
        assertThat(body.getMemberType()).isEqualTo("ADMIN");
        System.out.println(body.getToken());
    }

    @Test
    @DisplayName("회원 목록 열람")
    void viewMembers() {
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
        ResponseEntity<SignupDtoRequest> response = restTemplate.postForEntity(url,dto,SignupDtoRequest.class);

        LoginDtoRequest dtos = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String urls = "http://localhost:"+port+"/auth/login";
        ResponseEntity<LoginDtoResponse> responses = restTemplate.postForEntity(urls,dtos,LoginDtoResponse.class);
        LoginDtoResponse body = responses.getBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+body.getToken());
        HttpEntity request = new HttpEntity(headers);
        String requestUrl = "http://localhost:"+port+"/auth/view-members";
        ResponseEntity<List> responseFinal = restTemplate.exchange(requestUrl,HttpMethod.GET,request,List.class);

        LinkedHashMap<String,Object> lhm = (LinkedHashMap<String, Object>) responseFinal.getBody().get(0);
        assertThat(lhm.get("email").toString()).isEqualTo("test@test.com");
        assertThat(lhm.get("memberBelong").toString()).isEqualTo("Student");
        assertThat(lhm.get("memberType").toString()).isEqualTo("ADMIN");
        assertThat(lhm.get("name").toString()).isEqualTo("test_name");
        assertThat(lhm.get("admin")).isEqualTo(false);
    }

}