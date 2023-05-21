package com.webkit640.backend.repository.repository;

import com.webkit640.backend.entity.Board;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board,Integer>, JpaSpecificationExecutor<Board> {
    Board findById(int id);
    List<Board> findAll(Specification<Board> keyword);
}
