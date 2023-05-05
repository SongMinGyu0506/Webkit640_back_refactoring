package com.webkit640.backend.controller;

import com.webkit640.backend.dto.ApplicationDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Applicant;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.service.ApplicationService;
import com.webkit640.backend.service.FileEntityService;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
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

    @PostMapping(value = "",consumes = {"multipart/form-data"})
    public ResponseEntity<?> apply(@AuthenticationPrincipal int id, @RequestPart MultipartFile multipartFile, @RequestPart ApplicationDto dto) throws IOException {
        HashMap<String,Object> tmp = applicationService.saveEntity(id, ApplicationDto.dtoToEntity(dto));
        FileEntity fileEntity = fileEntityService.saveApplicationFile(multipartFile, (Applicant) tmp.get("applicant"), (Member) tmp.get("member"));

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(tmp.get("applicationId"))
                .toUri())
                .body(
                        ResponseWrapper.addObject(ApplicationDto.ApplicationResponseDto
                                .entityToDto(
                                        (Applicant) tmp.get("applicant"), fileEntity),
                                HttpStatus.CREATED)
                );
    }
    @GetMapping(value = "")
    public ResponseEntity<?> downloadApplication(@AuthenticationPrincipal int id, @RequestParam String email) throws UnsupportedEncodingException {
        Map<String, Object> map = fileEntityService.applicationDownload(id, email);

        File file = (File) map.get("file");
        Resource resource = (Resource) map.get("resource");
        String fileName = new String(file.getName().getBytes(StandardCharsets.UTF_8),"ISO-8859-1");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName +"\";")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                .body(resource);
    }
}