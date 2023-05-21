package com.webkit640.backend.repository.repository;

import com.webkit640.backend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity,Integer> {
    FileEntity findByMemberId(int memberId);
    List<FileEntity> findByBoardId(int boardId);
    void deleteByBoardId(int boardId);
}
