package com.webkit640.backend.repository.repository;

import com.webkit640.backend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface FileEntityRepository extends JpaRepository<FileEntity,Integer> {
    FileEntity findByMemberId(int memberId);
    List<FileEntity> findByBoardId(int boardId);
    @Transactional
    void deleteByBoardId(int boardId);

    FileEntity findByFilePathContains(String filePathLike);

    @Transactional
    void deleteById(int id);
}
