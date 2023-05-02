package com.webkit640.backend.service;

import com.webkit640.backend.config.exception.*;
import com.webkit640.backend.config.security.TokenProvider;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    private final OAuthLoginService oAuthLoginService;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                             TokenProvider tokenProvider,
                             @Qualifier("OAuthLoginServiceKakao") OAuthLoginService oAuthLoginService) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
        this.oAuthLoginService = oAuthLoginService;
    }

    @Override
    public Member create(Member member) {
        if (!memberRepository.existsMemberByEmail(member.getEmail())) {
            return memberRepository.save(member);
        }
        throw new AlreadyExistsException(String.format("[%s] is already used email",member.getEmail()));
    }

    @Override
    public List<Member> readAll() {
        return memberRepository.findAll();
    }

    @Override
    public HashMap<String,Object> getByCredentials(String email, String password) {
        Member member =  memberRepository.findByEmailAndPassword(email,password);
        HashMap<String,Object> returnData = new HashMap<>();
        if (member != null) {
            returnData.put("token",tokenProvider.create(member));
            returnData.put("member",member);
            return returnData;
        } else {
            throw new LoginFailedException(String.format("[%s] Login Failed", email));
        }
    }

    @Override
    public String getEmailById(int id) {
        return memberRepository.findById(id).getEmail();
    }

    @Override
    public void changeAdmin(String email,int id) {
        if(!memberRepository.findById(id).isAdmin()) {
            throw new NoAdminException("Not Admin User");
        }
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new NotFoundDataException("Not Found member");
        }
        member.setAdmin(!member.isAdmin());
        memberRepository.save(member);
    }

    @Override
    public Member getMemberById(int memberId) {
        Member member = memberRepository.findById(memberId);
        if (member == null) {
            throw new NotFoundDataException("Not Found member");
        }
        return member;
    }

    @Override
    public HashMap<String,Object> OAuthLogin(String code) {
        String oAuthEmail = oAuthLoginService.accessUser(oAuthLoginService.getAccessToken(code));
        Member member = memberRepository.findByEmail(oAuthEmail);
        HashMap<String,Object> returnData = new HashMap<>();
        if (member != null) {
            returnData.put("token",tokenProvider.create(member));
            returnData.put("member",member);
            return returnData;
        } else {
            throw new LoginFailedException("NOT REGISTERED USER");
        }
    }
}
