package com.webkit640.backend.service;

import com.webkit640.backend.config.security.TokenProvider;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,TokenProvider tokenProvider) {
        this.memberRepository = memberRepository;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Member create(Member member) {
        return !memberRepository.existsMemberByEmail(member.getEmail()) ? memberRepository.save(member) : null;
    }

    @Override
    public List<Member> readAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member read(int id) {
        return memberRepository.findById(id);
    }

    @Override
    public Member update(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public void delete(int id) {
        memberRepository.deleteById(id);
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
            return null;
        }
    }
}
