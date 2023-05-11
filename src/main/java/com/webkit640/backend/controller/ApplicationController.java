package com.webkit640.backend.controller;

import com.webkit640.backend.config.annotation.Admin;
import com.webkit640.backend.config.exception.FileServiceException;
import com.webkit640.backend.dto.ApplicationDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.service.logic.ApplicationService;
import com.webkit640.backend.service.logic.FileEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = {"Content-Disposition"})
@RestController
@RequestMapping("/application")
public class ApplicationController {
    private final ApplicationService applicationService;
    private final FileEntityService fileEntityService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, FileEntityService fileEntityService) {
        this.applicationService = applicationService;
        this.fileEntityService = fileEntityService;
    }

    @PostMapping(value = "/submit",consumes = {"multipart/form-data"})
    public ResponseEntity<?> apply(@AuthenticationPrincipal int id, @RequestPart MultipartFile multipartFile, @RequestPart ApplicationDto dto) throws IOException {
        HashMap<String,Object> tmp = applicationService.saveEntity(id, ApplicationDto.dtoToEntity(dto));
        FileEntity fileEntity = fileEntityService.saveApplicationFile(multipartFile, (Applicant) tmp.get("applicant"), (Member) tmp.get("member"));

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/application/{id}")
                .buildAndExpand(((Applicant)tmp.get("applicant")).getMember().getEmail())
                .toUri())
                .body(
                        ResponseWrapper.addObject(ApplicationDto.ApplicationResponseDto
                                .entityToDto(
                                        (Applicant) tmp.get("applicant"), fileEntity),
                                HttpStatus.CREATED)
                );
    }
    @Admin
    @GetMapping("/file/{email}")
    public ResponseEntity<?> adminDownloadApplication(@AuthenticationPrincipal int id, @PathVariable String email) {
        Map<String, Object> map = fileEntityService.applicationDownload(id, email);

        File file = (File) map.get("file");
        Resource resource = (Resource) map.get("resource");
        String fileName = new String(file.getName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName +"\";")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                .body(resource);
    }
    @Admin
    @GetMapping("/file")
    public ResponseEntity<?> adminZipDownloadApplication(@AuthenticationPrincipal int id) {
        Resource file = fileEntityService.filesToZip();
        try {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+file.getFile().getName() + "\"")
                    .header(HttpHeaders.CONTENT_LENGTH,String.valueOf(file.getFile().length()))
                    .header(HttpHeaders.CONTENT_TYPE,MediaType.APPLICATION_OCTET_STREAM.toString())
                    .body(file);
        } catch (IOException e) {
            throw new FileServiceException("controller get file error");
        }
    }

    @Admin
    @PatchMapping("/selection")
    public ResponseEntity<?> adminSelectionApplicants(@AuthenticationPrincipal int id, @RequestBody ApplicationDto.SelectionRequestDto emails) {
        applicationService.adminSelection(emails.getEmails());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/selection/confirmation")
    public ResponseEntity<?> selectionConfirmation(@AuthenticationPrincipal int id) {
        applicationService.selectionConfirmation(id);
        return ResponseEntity.noContent().build();
    }

    @Admin
    @GetMapping("")
    public ResponseEntity<?> viewApplicantList(
            @AuthenticationPrincipal int id,
            @RequestParam(required = false) String year,
            @RequestParam(required = false) String school,
            @RequestParam(required = false) String major) {
        return ResponseEntity.ok().body(ResponseWrapper.addObject(
                ApplicationDto.ApplicantListResponseDto.listEntityToDto(applicationService.getApplicantList(year,school,major)),HttpStatus.OK));
    }

    @Admin
    @GetMapping("/{email}")
    public ResponseEntity<?> viewApplicant(@AuthenticationPrincipal int id, @PathVariable String email) {
        return ResponseEntity.ok().body(ResponseWrapper.addObject(
                ApplicationDto.ApplicationResponseDto.listEntityToDto(applicationService.getApplicantList(email)),HttpStatus.OK));
    }
}
