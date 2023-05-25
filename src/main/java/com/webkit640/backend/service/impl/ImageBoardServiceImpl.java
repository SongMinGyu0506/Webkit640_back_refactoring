package com.webkit640.backend.service.impl;

import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.entity.Image;
import com.webkit640.backend.repository.repository.FileEntityRepository;
import com.webkit640.backend.repository.repository.ImageRepository;
import com.webkit640.backend.repository.repository.MemberRepository;
import com.webkit640.backend.repository.spec.ImageSpec;
import com.webkit640.backend.service.logic.FileEntityService;
import com.webkit640.backend.service.logic.ImageBoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ImageBoardServiceImpl implements ImageBoardService {

    @Value("${file_dir}")
    private String fileDir;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final FileEntityService fileEntityService;
    private final FileEntityRepository fileEntityRepository;

    @Value("${imageUploadPath}")
    private String imageDir;

    @Autowired
    public ImageBoardServiceImpl(MemberRepository memberRepository, ImageRepository imageRepository, FileEntityService fileEntityService, FileEntityRepository fileEntityRepository) {
        this.memberRepository = memberRepository;
        this.imageRepository = imageRepository;
        this.fileEntityService = fileEntityService;
        this.fileEntityRepository = fileEntityRepository;
    }

    @Override
    public Image saveImageBoard(MultipartFile file, Image imageBoard, int userId) {
        imageBoard.setImagePath(fileEntityService.saveImage(file, userId));
        imageBoard.setMember(memberRepository.findById(userId));
        imageRepository.save(imageBoard);
        return imageBoard;
    }

    @Override
    public List<Image> readImageBoard(String title, String author) {
        Specification<Image> spec = (r,q,c) -> null;
        if (title!= null) {
            spec = spec.and(ImageSpec.likeTitle(title));
        } else if (author!= null) {
            spec = spec.and(ImageSpec.equalUser(author));
        }
        return imageRepository.findAll(spec);
    }

    @Override
    public Image readImageBoard(int id) {
        return imageRepository.findById(id);
    }

    @Override
    public Image updateImageBoard(Image imageBoard) {
        return null;
    }

    @Override
    public void deleteImageBoard(int imageId) {
        fileEntityService.fileDelete(imageRepository.findById(imageId).getImagePath().split("/")[2]);
        imageRepository.deleteById(imageId);
    }
}
