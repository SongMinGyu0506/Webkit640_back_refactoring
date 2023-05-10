package com.webkit640.backend.service.impl;

import com.webkit640.backend.config.exception.ApplicantLogicException;
import com.webkit640.backend.config.exception.NotFoundDataException;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.entity.Trainee;
import com.webkit640.backend.repository.ApplicantRepository;
import com.webkit640.backend.repository.MemberRepository;
import com.webkit640.backend.repository.TraineeRepository;
import com.webkit640.backend.service.logic.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {
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
        applicant.setApply(true);
        member.setApplicant(applicant);

        Applicant resultApplicant = applicantRepository.save(applicant);
        Member resultMember = memberRepository.save(member);

        HashMap<String,Object> result = new HashMap<>();
        result.put("applicant",resultApplicant);
        result.put("member",resultMember);
        result.put("applicationId",resultApplicant.getId());

        return result;
    }

    @Override
    public void adminSelection(List<String> emails) {
        emails.stream().forEach(email -> {
            Member member = memberRepository.findByEmail(email);
            if (member == null) {
                throw new NotFoundDataException("해당 이메일을 가진 지원자가 없습니다.");
            }
            Applicant applicant = applicantRepository.findByMemberId(member.getId());
            applicant.setAdminApply(!applicant.isAdminApply());
            applicantRepository.save(applicant);
        });
    }

    @Override
    public void selectionConfirmation(int id) {
        Applicant byMemberId = applicantRepository.findByMemberId(id);
        if (byMemberId == null) {
            throw new NotFoundDataException("Not Applicant");
        }
        byMemberId.setSelect(!byMemberId.isSelect());
        applicantRepository.save(byMemberId);
    }
}
