package com.webkit640.backend.controller;


import com.webkit640.backend.config.annotation.Admin;
import com.webkit640.backend.dto.ImageBoardDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Image;
import com.webkit640.backend.service.logic.ImageBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = {"Content-Disposition"})
@RestController
@RequestMapping("/image")
@Slf4j
public class ImageBoardController {
    private final ImageBoardService imageBoardService;

    @Autowired
    public ImageBoardController(ImageBoardService imageBoardService) {
        this.imageBoardService = imageBoardService;
    }

    @Admin
    @GetMapping("")
    public ResponseEntity<?> getImageBoardList(@AuthenticationPrincipal int id,
                                               @RequestParam(required = false) String title,
                                               @RequestParam(required = false) String name) {

        return ResponseEntity.ok().body(ResponseWrapper.addObject(
                ImageBoardDto.SaveImageDto.toDto(imageBoardService.readImageBoard(title,name))
                , HttpStatus.OK));
    }

    @Admin
    @GetMapping("/{imageId}")
    public ResponseEntity<?> getImageBoard(@AuthenticationPrincipal int id, @PathVariable int imageId) {

        return ResponseEntity.ok().body(ResponseWrapper.addObject(
                ImageBoardDto.SaveImageDto.toDto(imageBoardService.readImageBoard(imageId))
                , HttpStatus.OK));
    }

    @Admin
    @PostMapping("")
    public ResponseEntity<?> addImageBoard(@AuthenticationPrincipal int id, @RequestPart ImageBoardDto.SaveImageDto dto, @RequestPart MultipartFile image) {
        Image board = imageBoardService.saveImageBoard(image, ImageBoardDto.SaveImageDto.toEntity(dto), id);

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(board.getId()).toUri())
                .body(ResponseWrapper.addObject(ImageBoardDto.SaveImageDto.toDto(board), HttpStatus.OK));
    }

    @Admin
    @DeleteMapping("/{imageId}")
    public ResponseEntity<?> deleteImageBoard(@AuthenticationPrincipal int id, @PathVariable int imageId) {
        imageBoardService.deleteImageBoard(imageId);
        return ResponseEntity.noContent().build();
    }
}
