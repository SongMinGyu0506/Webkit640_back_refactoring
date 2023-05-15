package com.webkit640.backend.controller;

import com.webkit640.backend.config.security.TokenProvider;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.service.logic.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.test.annotation.Commit;

public class WithAccountSecurityContextFactory implements WithSecurityContextFactory<WithAccount> {

    @Autowired
    MemberService memberService;
    @Autowired
    TokenProvider tokenProvider;


    @Override
    @Commit
    public SecurityContext createSecurityContext(WithAccount annotation) {
        Member member = Member.builder()
                .applicant(null)
                .boards(null)
                .counsels(null)
                .email(annotation.value())
                .memberBelong("Belong")
                .memberType("Type")
                .name("Name")
                .isAdmin(true)
                .password("1234")
                .build();

        memberService.create(member);

        int userId = tokenProvider.validateAndGetUserId(tokenProvider.create(member));

        AbstractAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId,null, AuthorityUtils.NO_AUTHORITIES);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        return context;
    }
}
