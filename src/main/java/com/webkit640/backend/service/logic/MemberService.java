package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Component
public interface MemberService {
    Member create(Member member);
    List<Member> readAll();
    HashMap<String,Object> getByCredentials(String email, String password);
    String getEmailById(int id);
    void changeAdmin(String email,int id);

    Member getMemberById(int memberId);
    HashMap<String,Object> OAuthLogin(String code);
}
