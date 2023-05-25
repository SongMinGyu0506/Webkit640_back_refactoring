package com.webkit640.backend.dto;

import com.webkit640.backend.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class ImageBoardDto {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveImageDto {
        private int id;
        private String name;
        private String title;
        private String imagePath;

        public static Image toEntity(SaveImageDto dto) {
            return Image.builder()
                  .title(dto.getTitle())
                  .imagePath(dto.getImagePath())
                  .build();
        }
        public static SaveImageDto toDto(Image image) {
            return SaveImageDto.builder()
                    .id(image.getId())
                    .name(image.getMember().getName())
                    .title(image.getTitle())
                    .imagePath(image.getImagePath())
                    .build();
        }
        public static List<SaveImageDto> toDto(List<Image> images) {
            return images.stream().map(image ->
                    SaveImageDto.builder()
                            .id(image.getId())
                            .name(image.getMember().getName())
                            .imagePath(image.getImagePath())
                            .title(image.getTitle())
                            .build()).toList();
        }
    }
}
