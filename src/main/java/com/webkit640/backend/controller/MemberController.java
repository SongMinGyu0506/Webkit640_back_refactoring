package com.webkit640.backend.controller;


import com.webkit640.backend.config.exception.AlreadyExistsException;
import com.webkit640.backend.config.exception.LoginFailedException;
import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.AllMemberDto;
import com.webkit640.backend.dto.response.LoginDtoResponse;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignupDtoRequest signupDto) {
        Member member = memberService.create(SignupDtoRequest.dtoToEntity(signupDto));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDtoRequest loginDto) {
        HashMap<String,Object> data = memberService.getByCredentials(loginDto.getEmail(), loginDto.getPassword());
        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(LoginDtoResponse.entityToDto((Member)data.get("member"),(String)data.get("token")), HttpStatus.OK));
    }

    @GetMapping("/view-members")
    public ResponseEntity<?> viewMembers(@AuthenticationPrincipal int id) {
        return ResponseEntity.ok().body(ResponseWrapper.addObject(
                AllMemberDto.entityToDtos(memberService.readAll()),HttpStatus.OK
        ));
    }

    @PatchMapping("/admin-change")
    public ResponseEntity<?> changeAdmin(@AuthenticationPrincipal int id, @RequestBody AllMemberDto dto) {
        memberService.changeAdmin(dto.getEmail(),id);
        return ResponseEntity.noContent().build();
    }
}
