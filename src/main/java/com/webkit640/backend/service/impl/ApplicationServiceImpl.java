package com.webkit640.backend.service.impl;

import com.webkit640.backend.config.exception.ApplicantLogicException;
import com.webkit640.backend.config.exception.NotFoundDataException;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.entity.Trainee;
import com.webkit640.backend.repository.ApplicantRepository;
import com.webkit640.backend.repository.ApplicantSpec;
import com.webkit640.backend.repository.MemberRepository;
import com.webkit640.backend.repository.TraineeRepository;
import com.webkit640.backend.service.logic.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
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
        if (emails == null) {
            throw new NotFoundDataException("이메일리스트 데이터가 없습니다.");
        }
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
    public void selectionConfirmation(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new NotFoundDataException("등록된 사용자가 아님 "+email);
        } else {
            Applicant byMemberId = member.getApplicant();
            if (byMemberId == null) {
                throw new NotFoundDataException("등록된 지원자가 아님"+email);
            }
            byMemberId.setSelect(!byMemberId.isSelect());
            applicantRepository.save(byMemberId);
        }
    }

    @Override
    public List<Applicant> getApplicantList(String year, String school, String major) {
        Specification<Applicant> spec = (root, query, criteriaBuilder) -> null;
        if (year != null) {
            spec = spec.and(ApplicantSpec.likeCreateDate(year+"-"));
        } else if (school != null) {
            spec = spec.and(ApplicantSpec.likeSchool(school));
        } else if (major != null) {
            spec = spec.and(ApplicantSpec.likeMajor(major));
        }

        return applicantRepository.findAll(spec);
    }

    @Override
    public List<Applicant> getApplicantList(String email) {
        Specification<Applicant> spec = (root, query, criteriaBuilder) -> null;
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            throw new NotFoundDataException("Not found User data");
        }
        spec = spec.and(ApplicantSpec.equalMemberId(member.getId()));
        return applicantRepository.findAll(spec);
    }
}
