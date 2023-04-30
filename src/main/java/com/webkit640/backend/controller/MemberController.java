package com.webkit640.backend.controller;


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
        return member != null ?
                ResponseEntity.created(
                        ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(member.getId())
                                .toUri()
                ).build():
                new ResponseEntity<>(ResponseWrapper.addObject("Already Used Email",HttpStatus.CONFLICT),HttpStatus.valueOf(409));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDtoRequest loginDto) {
        HashMap<String,Object> data = memberService.getByCredentials(loginDto.getEmail(), loginDto.getPassword());
        return data != null ? ResponseEntity.ok().body(
                ResponseWrapper.addObject(LoginDtoResponse.entityToDto((Member)data.get("member"),(String)data.get("token")),
                        HttpStatus.OK)
        ):
                ResponseEntity.badRequest().body(ResponseWrapper.addObject("Login Failed", HttpStatus.BAD_REQUEST));
    }

    @GetMapping("/view-members")
    public ResponseEntity<?> viewMembers(@AuthenticationPrincipal int id) {
        return ResponseEntity.ok().body(ResponseWrapper.addObject(
                AllMemberDto.entityToDtos(memberService.readAll()),HttpStatus.OK
        ));
    }
    @PatchMapping("/admin-change")
    public ResponseEntity<?> changeAdmin(@AuthenticationPrincipal int id, @RequestBody AllMemberDto dto) {
        int code = memberService.changeAdmin(dto.getEmail(),id);
        return code == 0 ?
                ResponseEntity.noContent().build() :
                code == 1 ?
                new ResponseEntity<>(ResponseWrapper.addObject("That user does not exist.",HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST):
                        new ResponseEntity<>(ResponseWrapper.addObject("You do not have permission to use that function.",HttpStatus.FORBIDDEN),HttpStatus.FORBIDDEN);

    }
}
