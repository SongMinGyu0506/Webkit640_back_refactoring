package com.webkit640.backend.service.impl;


import com.webkit640.backend.config.exception.NotFoundDataException;
import com.webkit640.backend.entity.MainPageEntity;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.repository.MainPageRepository;
import com.webkit640.backend.repository.repository.MemberRepository;
import com.webkit640.backend.service.logic.MainPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MainPageServiceImpl implements MainPageService {
    private final MainPageRepository mainPageRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public MainPageServiceImpl(MainPageRepository mainPageRepository, MemberRepository memberRepository) {
        this.mainPageRepository = mainPageRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public MainPageEntity read() {
        List<MainPageEntity> all = mainPageRepository.findAll();
        if (all.size() == 0) {
            throw new NotFoundDataException("메인페이지의 데이터가 없습니다.");
        } else{
            return all.get(0);
        }
    }

    @Override
    public MainPageEntity create(MainPageEntity mainPageEntity) {
        return mainPageRepository.save(mainPageEntity);
    }

    @Override
    public void adminModification(int memberId) {
        Member member = memberRepository.findById(memberId);
        member.setAdmin(true);
        memberRepository.save(member);
    }
}
