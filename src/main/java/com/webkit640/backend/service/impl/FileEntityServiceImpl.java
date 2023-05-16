package com.webkit640.backend.service.impl;

import com.webkit640.backend.config.exception.FileServiceException;
import com.webkit640.backend.config.exception.NoAdminException;
import com.webkit640.backend.config.exception.NotFoundDataException;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.FileEntityRepository;
import com.webkit640.backend.repository.MemberRepository;
import com.webkit640.backend.service.logic.FileEntityService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Slf4j
public class FileEntityServiceImpl implements FileEntityService {

    @Value("${file_dir}")
    private String fileDir;
    @Value("${mime_type}")
    private ArrayList<String> mimeTypeList;

    private final FileEntityRepository fileEntityRepository;
    private final MemberRepository memberRepository;
    private final ResourceLoader resourceLoader;

    @Autowired
    public FileEntityServiceImpl(FileEntityRepository fileEntityRepository, MemberRepository memberRepository, ResourceLoader resourceLoader) {
        this.fileEntityRepository = fileEntityRepository;
        this.memberRepository = memberRepository;
        this.resourceLoader = resourceLoader;
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
        String extension = originalName.substring(originalName.lastIndexOf(".")+1);
        String saveName = applicant.getMember().getEmail() + "_apply."+extension;
        if (mimeTypeList.contains(mimeType)) {
            FileEntity file = FileEntity.builder()
                    .applicant(applicant)
                    .fileExtension(extension)
                    .fileName(saveName)
                    .filePath(yearFolderName+"/")
                    .fileType("APPLY")
                    .member(member)
                    .build();
            files.transferTo(new File(yearFolderName+"/"+saveName));
            return fileEntityRepository.save(file);
        } else {
            throw new FileServiceException("Only pdf,hwp,docx");
        }
    }

    @Override
    public Map<String,Object> applicationDownload(int id, String email) {
        Map<String,Object> result = new HashMap<>();
        if (!memberRepository.findById(id).isAdmin()) {
            throw new NoAdminException("관리자가 아닙니다.");
        }
        try {
            FileEntity file = fileEntityRepository.findByMemberId(memberRepository.findByEmail(email).getId());
            result.put("resource",resourceLoader.getResource("file:"+file.getFilePath()+file.getFileName()));
            result.put("file",resourceLoader.getResource("file:"+file.getFilePath()+file.getFileName()).getFile());
            result.put("fileName",file.getFileName());
            return result;
        } catch (Exception e) {
            throw new FileServiceException("Not found File(Application)");
        }
    }

    @Override
    public Resource filesToZip() {
        String name = getGenerationName(LocalDate.now());
        String path = name+"\\"+name.substring(name.length()-6)+".zip";
        File existFile = new File(path);
        if (existFile.exists()) {existFile.delete();}
        File[] applications = new File(name).listFiles();
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(path))) {
            Arrays.stream(applications).forEach(file -> {
                try (FileInputStream fis = new FileInputStream(file)) {
                    ZipEntry zipEntry = new ZipEntry(file.getName());
                    zos.putNextEntry(zipEntry);

                    byte[] bytes = new byte[104857600];
                    int length;
                    while ((length = fis.read(bytes)) >= 0) {
                        zos.write(bytes,0,length);
                    }
                    zos.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new FileServiceException("Zip file processing error");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            throw new FileServiceException("ZIP File output stream error");
        }
        return resourceLoader.getResource("file:"+path);
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
