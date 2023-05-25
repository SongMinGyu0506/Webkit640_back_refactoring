package com.webkit640.backend.repository.repository;

import com.webkit640.backend.entity.Calendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface CalendarRepository extends JpaRepository<Calendar,Integer> {
    List<Calendar> findAll();

    @Transactional
    void deleteById(int id);
}
