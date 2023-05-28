package com.webkit640.backend.controller;


import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.AllMemberDto;
import com.webkit640.backend.dto.response.LoginDtoResponse;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.service.logic.MemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member")
    @ApiOperation(value = "회원가입 컨트롤러")
    public ResponseEntity<?> signUp(@RequestBody SignupDtoRequest signupDto) {
        Member member = memberService.create(SignupDtoRequest.dtoToEntity(signupDto));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(member.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("")
    @ApiOperation(value = "로그인 컨트롤러",notes = "<h2>JWT 토큰을 반환합니다. 해당 토큰을 헤더에 첨부하여 다른 기능을 수행해야합니다.<h2>")
    public ResponseEntity<?> login(@RequestBody LoginDtoRequest loginDto) {
        HashMap<String,Object> data = memberService.getByCredentials(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(LoginDtoResponse.entityToDto((Member)data.get("member"),(String)data.get("token")), HttpStatus.OK));
    }

    @GetMapping("/member")
    @ApiOperation(value = "View all members")
    public ResponseEntity<?> viewMembers(@AuthenticationPrincipal int id) {
        return ResponseEntity.ok().body(ResponseWrapper.addObject(
                AllMemberDto.entityToDtos(memberService.readAll()),HttpStatus.OK
        ));
    }
    @GetMapping("/member/{member_id}")
    @ApiOperation(value = "View a member")
    public ResponseEntity<?> viewMember(@AuthenticationPrincipal int id, @PathVariable("member_id") int memberId) {
        return ResponseEntity.ok().body(AllMemberDto.entityToDtos(memberService.getMemberById(memberId)));
    }

    @GetMapping("/kakao")
    @ApiOperation(value = "카카오 OAuth2.0 로그인 컨트롤러")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) {
        HashMap<String, Object> data = memberService.OAuthLogin(code);
        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(LoginDtoResponse.entityToDto((Member)data.get("member"),(String)data.get("token")),HttpStatus.OK));
    }

    @PatchMapping("/admin-change")
    @ApiOperation(value = "관리자 여부 결정 컨트롤러")
    public ResponseEntity<?> changeAdmin(@AuthenticationPrincipal int id, @RequestParam String email) {
        memberService.changeAdmin(email,id);
        return ResponseEntity.noContent().build();
    }

}
