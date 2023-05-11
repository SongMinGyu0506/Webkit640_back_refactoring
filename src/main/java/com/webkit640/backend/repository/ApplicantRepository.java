package com.webkit640.backend.repository;

import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Member;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,Integer>, JpaSpecificationExecutor<Applicant> {
    Applicant findByMemberId(int id);
    List<Applicant> findAll(Specification<Applicant> keyword);
}
