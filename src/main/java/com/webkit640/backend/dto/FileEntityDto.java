package com.webkit640.backend.dto;

import com.webkit640.backend.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileEntityDto {
    private int id;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardCreateResponseDto {
        private int id;
        private String fileName;
        private String fileExtension;
        private String filePath;
        private String fileType;

        public static List<BoardCreateResponseDto> entityToDto(List<FileEntity> files) {
            List<BoardCreateResponseDto> result = new ArrayList<>();
            files.forEach(file -> result.add(
                    BoardCreateResponseDto.builder()
                            .id(file.getId())
                            .fileExtension(file.getFileExtension())
                            .fileName(file.getFileName())
                            .filePath(file.getFilePath())
                            .fileType(file.getFileType())
                            .build()
            ));
            return result;
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardResponseDto {
        private int fileId;
        private String fileName;
        private String fileExtension;
        private String filePath;
        private String fileType;

        public static List<BoardResponseDto> entityToDto(List<FileEntity> files) {
            List<BoardResponseDto> result = new ArrayList<>();
            files.forEach(file -> result.add(
                    BoardResponseDto.builder()
                            .fileExtension(file.getFileExtension())
                            .fileName(file.getFileName())
                            .filePath(file.getFilePath())
                            .fileType(file.getFileType())
                            .fileId(file.getId())
                            .build()
            ));
            return result;
        }
    }
}
