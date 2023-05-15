package com.webkit640.backend.repository;

import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Trainee;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TraineeRepository extends JpaRepository<Trainee,Integer>, JpaSpecificationExecutor<Trainee> {
    Trainee findById(int id);
    List<Trainee> findAll(Specification<Trainee> keyword);
}