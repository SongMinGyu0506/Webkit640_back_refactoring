package com.webkit640.backend.controller;

import com.webkit640.backend.repository.repository.ApplicantRepository;
import com.webkit640.backend.repository.repository.BoardRepository;
import com.webkit640.backend.repository.repository.FileEntityRepository;
import com.webkit640.backend.repository.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RepositoryControl {

    private final FileEntityRepository fileEntityRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final ApplicantRepository applicantRepository;

    @Autowired
    public RepositoryControl(FileEntityRepository fileEntityRepository, BoardRepository boardRepository, MemberRepository memberRepository, ApplicantRepository applicantRepository) {
        this.fileEntityRepository = fileEntityRepository;
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.applicantRepository = applicantRepository;
    }

    public void init() {
        fileEntityRepository.deleteAll();
        boardRepository.deleteAll();
        applicantRepository.deleteAll();
        memberRepository.deleteAll();
    }
}
