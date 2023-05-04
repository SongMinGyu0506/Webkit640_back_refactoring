package com.webkit640.backend.service;

import com.webkit640.backend.config.exception.NotFoundDataException;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.ApplicantRepository;
import com.webkit640.backend.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService{
    private final MemberRepository memberRepository;
    private final ApplicantRepository applicantRepository;

    public ApplicationServiceImpl(MemberRepository memberRepository, ApplicantRepository applicantRepository) {
        this.memberRepository = memberRepository;
        this.applicantRepository = applicantRepository;
    }

    @Override
    public HashMap<String,Object> saveEntity(int id, Applicant applicant) {
        Member member = memberRepository.findById(id);
        if (member == null) {
            throw new NotFoundDataException("User Error");
        }
        applicant.setMember(member);
        member.setApplicant(applicant);

        Applicant resultApplicant = applicantRepository.save(applicant);
        Member resultMember = memberRepository.save(member);

        HashMap<String,Object> result = new HashMap<>();
        result.put("applicant",resultApplicant);
        result.put("member",resultMember);
        result.put("applicationId",resultApplicant.getId());

        return result;
    }
}
