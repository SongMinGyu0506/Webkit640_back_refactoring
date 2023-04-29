package com.webkit640.backend.controller;


import com.webkit640.backend.dto.request.LoginDtoRequest;
import com.webkit640.backend.dto.request.SignupDtoRequest;
import com.webkit640.backend.dto.response.AllMemberDtoResponse;
import com.webkit640.backend.dto.response.LoginDtoResponse;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

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
        return member != null ?
                ResponseEntity.ok().body(ResponseWrapper.addObject(SignupDtoRequest.signupDtoResponse(member))) :
                ResponseEntity.badRequest().body("failed");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDtoRequest loginDto) {
        HashMap<String,Object> data = memberService.getByCredentials(loginDto.getEmail(), loginDto.getPassword());
        return data != null ? ResponseEntity.ok().body(
                LoginDtoResponse.entityToDto((Member)data.get("member"),(String)data.get("token"))
        ) :
                ResponseEntity.badRequest().body("login Failed");
    }

    @GetMapping("/view-members")
    public ResponseEntity<?> viewMembers(@AuthenticationPrincipal int id) {
        return ResponseEntity.ok().body(AllMemberDtoResponse.entityToDtos(memberService.readAll()));
    }
}
