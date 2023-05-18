package com.webkit640.backend.dto;

import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BoardDto {
    private String id;
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateBoardDto {
        private String type;
        private String title;
        private String author;
        private String content;

        public static Board dtoToEntity(CreateBoardDto dto) {
            return Board.builder()
                    .cnt(0)
                    .boardType(dto.getType())
                    .title(dto.getTitle())
                    .content(dto.getContent())
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createBoardResponseDto {
        private String type;
        private String title;
        private String author;
        private String content;
        private boolean isAdd;
        private List<FileEntityDto.BoardCreateResponseDto> files;

        public static createBoardResponseDto entityToDto(List<FileEntity> files, Board board) {
            return createBoardResponseDto.builder()
                    .isAdd(board.isAdd())
                    .files(FileEntityDto.BoardCreateResponseDto.entityToDto(files))
                    .title(board.getTitle())
                    .type(board.getBoardType())
                    .author(board.getMember().getName())
                    .content(board.getContent())
                    .build();
        }
    }
}
