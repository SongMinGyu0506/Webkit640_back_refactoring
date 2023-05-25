package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Image;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
public interface ImageBoardService {
    Image saveImageBoard(MultipartFile file, Image imageBoard, int userId);
    List<Image> readImageBoard(String title, String author);
    Image readImageBoard(int id);
    Image updateImageBoard(Image imageBoard);
    void deleteImageBoard(int imageId);
}
