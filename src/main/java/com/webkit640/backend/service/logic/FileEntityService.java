package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.entity.Member;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public interface FileEntityService {
    FileEntity saveApplicationFile(MultipartFile files, Applicant applicant, Member member) throws IOException;
    Map<String,Object> applicationDownload(int id, String email);
    Resource filesToZip();
    List<FileEntity> saveBoardFile(List<MultipartFile> files, int boardId, int member);
    List<FileEntity> findByBoardId(Board board);
    String saveImage(MultipartFile file);
    Map<String,Object> boardAttachedFileDownload(int fileId);
    void updateBoardFiles(int boardId, List<MultipartFile> files);
}
