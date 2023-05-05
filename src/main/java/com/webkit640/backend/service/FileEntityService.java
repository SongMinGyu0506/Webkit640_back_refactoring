package com.webkit640.backend.service;

import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.entity.Member;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public interface FileEntityService {
    FileEntity saveApplicationFile(MultipartFile files, Applicant applicant, Member member) throws IOException;
    Map<String,Object> applicationDownload(int id, String email);
    String filesToZip();
    FileEntity saveBoardFile(MultipartFile files, Board board, Member member);
    List<FileEntity> findByBoardId(Board board);
    String saveImage(MultipartFile file);
}
