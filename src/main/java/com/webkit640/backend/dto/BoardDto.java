package com.webkit640.backend.dto;

import com.webkit640.backend.entity.Board;
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
public class BoardDto {
    private String id;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListBoardId {
        private List<Integer> boardId;
    }

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
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentDto {
        private int boardId;
        private String author;
        private String comment;
        private String date;

        @Data
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class CommentUpdateDto {
            private String comment;
        }
        public static List<CommentDto> entityToDto(List<Board> entities) {
            List<CommentDto> commentDtos = new ArrayList<>();
            entities.forEach(entity -> commentDtos.add(
                    CommentDto.builder()
                            .boardId(entity.getId())
                            .comment(entity.getContent())
                            .author(entity.getMember().getName())
                            .date(entity.getCreateDate())
                            .build()
            ));
            return commentDtos;
        }
        public static Board dtoToEntity(CommentDto dto) {
            return Board.builder()
                    .title("comment")
                    .isAdd(true)
                    .content(dto.getComment())
                    .boardType("COMMENT")
                    .build();
        }
    }
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponseDto {
        private int id;
        private String title;
        private String author;
        private int cnt;
        private String date;
        private List<CommentDto> comments;
        private List<FileEntityDto.BoardResponseDto> files;

        public static List<ListResponseDto> entityToDto(List<Board> entities) {
            List<ListResponseDto> result = new ArrayList<>();
            entities.forEach(entity -> result.add(
                    ListResponseDto.builder()
                            .id(entity.getId())
                            .cnt(entity.getCnt())
                            .title(entity.getTitle())
                            .author(entity.getMember().getName())
                            .comments(CommentDto.entityToDto(entity.getBoards()))
                            .date(entity.getCreateDate())
                            .build()
            ));
            return result;
        }
        public static ListResponseDto entityToDto(Board entity) {
            return ListResponseDto.builder()
                    .id(entity.getId())
                    .cnt(entity.getCnt())
                    .title(entity.getTitle())
                    .author(entity.getMember().getName())
                    .date(entity.getCreateDate())
                    .comments(CommentDto.entityToDto(entity.getBoards()))
                    .files(FileEntityDto.BoardResponseDto.entityToDto(entity.getFiles()))
                    .build();
        }
    }
}
