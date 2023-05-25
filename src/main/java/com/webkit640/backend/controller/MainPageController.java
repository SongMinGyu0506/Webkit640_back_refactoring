package com.webkit640.backend.controller;

import com.webkit640.backend.config.annotation.Admin;
import com.webkit640.backend.dto.MainPageDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.service.logic.MainPageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = {"Content-Disposition"})
@RestController
@RequestMapping("/main-page")
@Slf4j
public class MainPageController {

    private final MainPageService mainPageService;

    @Autowired
    public MainPageController(MainPageService mainPageService) {
        this.mainPageService = mainPageService;
    }

    @GetMapping("")
    public ResponseEntity<?> getMainPage() {
        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(mainPageService.read(), HttpStatus.OK)
        );
    }
    @Admin
    @PostMapping("")
    public ResponseEntity<?> addMainPage(@AuthenticationPrincipal int id, @RequestBody MainPageDto dto) {
        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(mainPageService.create(MainPageDto.toEntity(dto)), HttpStatus.OK)
        );
    }
    @Admin
    @PatchMapping("/admin-change")
    public ResponseEntity<?> updateMainPage(@AuthenticationPrincipal int id, @RequestParam int memberId) {
        mainPageService.adminModification(memberId);
        return ResponseEntity.noContent().build();
    }
}
