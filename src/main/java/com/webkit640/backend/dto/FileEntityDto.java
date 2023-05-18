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
        private String fileName;
        private String fileExtension;
        private String filePath;
        private String fileType;

        public static List<BoardCreateResponseDto> entityToDto(List<FileEntity> files) {
            List<BoardCreateResponseDto> result = new ArrayList<>();
            files.forEach(file -> result.add(
                    BoardCreateResponseDto.builder()
                            .fileExtension(file.getFileExtension())
                            .fileName(file.getFileName())
                            .filePath(file.getFilePath())
                            .fileType(file.getFileType())
                            .build()
            ));
            return result;
        }
    }
}
