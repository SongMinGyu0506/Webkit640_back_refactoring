package com.webkit640.backend.repository;

import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,Integer> {
    Applicant findByMemberId(int id);
}
