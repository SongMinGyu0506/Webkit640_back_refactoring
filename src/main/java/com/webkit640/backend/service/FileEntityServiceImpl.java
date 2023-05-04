package com.webkit640.backend.service;

import com.webkit640.backend.config.exception.FileServiceException;
import com.webkit640.backend.config.exception.NotFoundDataException;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.FileEntityRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
public class FileEntityServiceImpl implements FileEntityService {

    @Value("${file_dir}")
    private String fileDir;

    private final FileEntityRepository fileEntityRepository;

    public FileEntityServiceImpl(FileEntityRepository fileEntityRepository) {
        this.fileEntityRepository = fileEntityRepository;
    }

    private String getGenerationName(LocalDate ld) {
        String year = Integer.toString(ld.getYear());
        String generation = ld.getMonthValue() <= 6 ? "-1":"-2";
        year += generation;
        return fileDir+year;
    }

    @Override
    public FileEntity saveApplicationFile(MultipartFile files, Applicant applicant, Member member) throws IOException {
        if (files.isEmpty()) {
            throw new NotFoundDataException("No MultipartFiles");
        }
        File folder = new File(fileDir);
        LocalDate ld = LocalDate.now();
        String yearFolderName = getGenerationName(ld);
        File yearFolder = new File(yearFolderName);

        if (!folder.exists()) {
            try {
                folder.mkdir();
                yearFolder.mkdir();
            } catch (Exception e) {
                throw new FileServiceException("folder make exception");
            }
        }
        if (!yearFolder.exists()) {
            try {
                yearFolder.mkdir();
            } catch (Exception e) {
                throw new FileServiceException("year folder make exception");
            }
        }
        Tika tika = new Tika();
        String originalName = files.getOriginalFilename();
        String mimeType = tika.detect(files.getBytes());
        log.info(mimeType);
        String[] mimeArr = {"application/pdf",
                "application/x-hwp",
                "application/haansofthwp",
                "application/vnd.hancom.hwp",
                "application/vnd.hancom.*",
                "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "application/x-tika-msoffice"};
        List<String> mimeList = new ArrayList<>(Arrays.asList(mimeArr));
        String extension = originalName.substring(originalName.lastIndexOf(".")+1);
        String saveName = applicant.getMember().getName() + "_apply_."+extension;
        if (mimeList.contains(mimeType)) {
            FileEntity file = FileEntity.builder()
                    .applicant(applicant)
                    .fileExtension(extension)
                    .fileName(saveName)
                    .filePath(yearFolderName+"/")
                    .fileType("APPLY")
                    .member(member)
                    .build();
            files.transferTo(new File(yearFolderName+"/"+saveName));
            FileEntity result = fileEntityRepository.save(file);
            return result;
        } else {
            throw new FileServiceException("Only pdf,hwp,docx");
        }
    }

    @Override
    public String findByMemberId(Member member) {
        return null;
    }

    @Override
    public String filesToZip() {
        return null;
    }

    @Override
    public FileEntity saveBoardFile(MultipartFile files, Board board, Member member) {
        return null;
    }

    @Override
    public List<FileEntity> findByBoardId(Board board) {
        return null;
    }

    @Override
    public String saveImage(MultipartFile file) {
        return null;
    }
}
