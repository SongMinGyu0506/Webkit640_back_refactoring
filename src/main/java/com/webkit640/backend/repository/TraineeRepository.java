package com.webkit640.backend.repository;

import com.webkit640.backend.entity.Trainee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TraineeRepository extends JpaRepository<Trainee,Integer> {
    Trainee findById(int id);
}
