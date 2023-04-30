package com.webkit640.backend.controller;

import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.AllMemberDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberControllerTest {

    @LocalServerPort
    int port;

    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;

    @BeforeEach
    void beforeInit() {
        memberRepository.deleteAll();
    }
    @AfterEach
    void init() {
        memberRepository.deleteAll();
    }
    @SuppressWarnings("unchecked")
    ResponseEntity<ResponseWrapper> makeAccount() {
        SignupDtoRequest dto = SignupDtoRequest.builder()
                .name("test_name")
                .email("test@test.com")
                .password("1234")
                .memberBelong("Student")
                .memberType("ADMIN")
                .build();
        String url = "http://localhost:" + port + "/auth/sign-up";
        return restTemplate.postForEntity(url,dto,ResponseWrapper.class);
    }
    @SuppressWarnings("unchecked")
    ResponseEntity<ResponseWrapper> makeLogin() {
        ResponseEntity<ResponseWrapper> response = makeAccount();
        LoginDtoRequest dtos = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String urls = "http://localhost:"+port+"/auth/login";
        response = restTemplate.postForEntity(urls,dtos,ResponseWrapper.class);
        return response;
    }
    @SuppressWarnings("unchecked")
    LinkedHashMap<String,Object> makeLoginBody() {
        ResponseEntity<ResponseWrapper> response = makeLogin();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return (LinkedHashMap<String, Object>) response.getBody().getData().get(0);
    }
    @SuppressWarnings("unchecked")
    List<LinkedHashMap<String,Object>> makeViewMembers() {
        ResponseEntity<ResponseWrapper> response = makeAccount();
        LinkedHashMap<String,Object> lhm = makeLoginBody();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+lhm.get("token"));
        HttpEntity request = new HttpEntity(headers);
        String requestUrl = "http://localhost:"+port+"/auth/view-members";
        response = restTemplate.exchange(requestUrl,HttpMethod.GET,request,ResponseWrapper.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return (List<LinkedHashMap<String, Object>>) response.getBody().getData().get(0);
    }

    ResponseEntity<ResponseWrapper> makeChangeAdmin() {
        restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
        LinkedHashMap<String,Object> lhm = makeLoginBody();
        AllMemberDto allMemberDto = AllMemberDto.builder()
                .email("test@test.com")
                .isAdmin(false)
                .memberBelong("testCorps")
                .name("test_a")
                .memberType("student")
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+lhm.get("token"));
        HttpEntity<AllMemberDto> request = new HttpEntity<>(allMemberDto,headers);
        String requestUrl = "http://localhost:"+port+"/auth/admin-change";
        return restTemplate.exchange(requestUrl,HttpMethod.PATCH,request,ResponseWrapper.class);
    }
    ResponseEntity<ResponseWrapper> secondChange(String email) {
        LoginDtoRequest dtos = LoginDtoRequest.builder()
                .email("test@test.com")
                .password("1234")
                .build();
        String urls = "http://localhost:"+port+"/auth/login";
        ResponseEntity<ResponseWrapper> response = restTemplate.postForEntity(urls,dtos,ResponseWrapper.class);
        LinkedHashMap<String, Object> lhm = (LinkedHashMap<String, Object>) response.getBody().getData().get(0);

        AllMemberDto allMemberDto = AllMemberDto.builder()
                .email(email)
                .isAdmin(false)
                .memberBelong("testCorps")
                .name("test_a")
                .memberType("student")
                .build();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization","Bearer "+lhm.get("token"));
        HttpEntity<AllMemberDto> request = new HttpEntity<>(allMemberDto,headers);
        String requestUrl = "http://localhost:"+port+"/auth/admin-change";
        return restTemplate.exchange(requestUrl,HttpMethod.PATCH,request,ResponseWrapper.class);

    }




    @Test
    @DisplayName("로컬 회원가입 테스트")
    @Order(1)
    void signUp() {
        ResponseEntity<ResponseWrapper> response = makeAccount();
        Member member = memberRepository.findByEmail("test@test.com");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(member.getName()).isEqualTo("test_name");
        assertThat(member.getMemberBelong()).isEqualTo("Student");
        assertThat(member.getMemberType()).isEqualTo("ADMIN");
        assertThat(member.getPassword()).isEqualTo("1234");
        assertThat(member.getEmail()).isEqualTo("test@test.com");
    }


    @Test
    @DisplayName("로컬 로그인 테스트")
    @Order(2)
    void login() {
        LinkedHashMap<String,Object> lhm = makeLoginBody();

        assertThat(lhm.get("email")).isEqualTo("test@test.com");
        assertThat(lhm.get("memberBelong")).isEqualTo("Student");
        assertThat(lhm.get("memberType")).isEqualTo("ADMIN");
        System.out.println(lhm.get("token"));
    }

    @Test
    @DisplayName("회원 목록 열람")
    @Order(3)
    void viewMembers() {
        List<LinkedHashMap<String,Object>> list = makeViewMembers();

        assertThat(list.get(0).get("email")).isEqualTo("test@test.com");
        assertThat(list.get(0).get("memberBelong")).isEqualTo("Student");
        assertThat(list.get(0).get("memberType")).isEqualTo("ADMIN");
        assertThat(list.get(0).get("name")).isEqualTo("test_name");
        assertThat(list.get(0).get("admin")).isEqualTo(false);
    }

    @Test
    @DisplayName("관리자 권한 부여 테스트")
    @Order(4)
    void adminChange() {
        assertThat(makeChangeAdmin().getStatusCodeValue()).isEqualTo(401);

        //관리자 권한 부여
        Member member = memberRepository.findByEmail("test@test.com");
        member.setAdmin(!member.isAdmin());
        memberRepository.save(member);

        assertThat(secondChange("test123@test.com").getStatusCodeValue()).isEqualTo(400);
        assertThat(secondChange("test@test.com").getStatusCodeValue()).isEqualTo(204);
    }
}