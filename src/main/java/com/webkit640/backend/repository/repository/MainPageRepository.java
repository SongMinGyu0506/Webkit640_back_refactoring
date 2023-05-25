package com.webkit640.backend.repository.repository;


import com.webkit640.backend.entity.MainPageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainPageRepository extends JpaRepository<MainPageEntity, Integer> {
}
