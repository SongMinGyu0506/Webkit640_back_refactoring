package com.webkit640.backend.controller;

import com.webkit640.backend.config.annotation.Admin;
import com.webkit640.backend.dto.BoardDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.service.logic.BoardService;
import com.webkit640.backend.service.logic.FileEntityService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.metadata.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", exposedHeaders = {"Content-Disposition"})
@RestController
@RequestMapping("/board")
@Slf4j
public class BoardController {
    private final BoardService boardService;
    private final FileEntityService fileEntityService;

    @Autowired
    public BoardController(BoardService boardService, FileEntityService fileEntityService) {
        this.boardService = boardService;
        this.fileEntityService = fileEntityService;
    }

    /**
     * 해당 컨트롤러는 게시글 id에 맞는 게시글 데이터를 반환하는 역할을 가진 컨트롤러이다.<br/>
     * 소스코드 작성자: 송민규
     * @param id 로그인 사용자
     * @param boardId 게시판 ID
     * @return HttpStatus is OK, boardId 값을 가진 게시글 하나만 데이터 반환 <br/>
     * HTTP STATUS: 200
     */
    @GetMapping("/{boardId}")
    @ApiOperation(value = "게시글 상세 컨트롤러", response = BoardDto.ListResponseDto.class,
    notes = "<h2>게시글 내용을 확인할 수 있습니다.</h2>")
    public ResponseEntity<?> getBoard(@AuthenticationPrincipal int id, @PathVariable int boardId) {
        return ResponseEntity.ok().body(ResponseWrapper.addObject(BoardDto.ListResponseDto.entityToDto(boardService.boardRead(boardId)),HttpStatus.OK));
    }


    /**
     * 해당 컨트롤러는 게시글 리스트를 반환하는 컨트롤러
     * @param id 로그인 사용자
     * @return {@link java.util.List}로 구성된 게시글 리스트 <br/>
     * HTTP STATUS: 200
     */
    @GetMapping("")
    @ApiOperation(value = "게시글 목록 컨트롤러", response = BoardDto.ListResponseDto.class,
    notes = "<h2>게시글 목록 및 검색을 수행 할 수 있습니다.</h2>")
    public ResponseEntity<?> getListBoard(@AuthenticationPrincipal int id,
                                          @RequestParam String type,
                                          @RequestParam(required = false) String author,
                                          @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(
                        BoardDto.ListResponseDto.entityToDto(boardService.boardRead(type, title, author,true)
                        ),
                        HttpStatus.OK));
    }

    /**
     * 게시글을 작성하는 컨트롤러
     * @param id 로그인 사용자
     * @param files 클라이언트에서 업로드하는 파일 데이터
     * @param dto 게시글 양식에 따라 작성한 텍스트 데이터
     * @return Response Header에 데이터가 저장된 URI를 반환하고, 작성된 데이터를 body에 간단하게 작성 <br/>
     * HTTP STATUS: 201
     */
    @PostMapping("")
    @ApiOperation(value = "게시글 생성 컨트롤러", response = BoardDto.createBoardResponseDto.class,
    notes = "<h2>게시글을 생성 할 수 있습니다.</h2>")
    public ResponseEntity<?> createBoard(@AuthenticationPrincipal int id, @RequestPart(required = false) List<MultipartFile> files, @RequestPart BoardDto.CreateBoardDto dto) {
        Board board = boardService.createBoard(BoardDto.CreateBoardDto.dtoToEntity(dto),id);
        List<FileEntity> fileEntities = fileEntityService.saveBoardFile(files, board.getId(), id,"BOARD");

        return ResponseEntity.
                created(ServletUriComponentsBuilder.fromCurrentRequest().
                        path("/{id}").buildAndExpand(board.getId()).toUri()).
                body(ResponseWrapper.addObject(BoardDto.createBoardResponseDto.entityToDto(fileEntities,board), HttpStatus.CREATED));
    }

    /**
     *
     * @param id 로그인 사용자
     * @param boardId 수정할 원본 게시글 ID
     * @param dto 수정할 데이터들
     * @return 204 NO CONTENT로 반환
     */
    @PatchMapping("/{boardId}")
    @ApiOperation(value = "게시글 수정 컨트롤러",notes = "<h2>게시글을 수정할 수 있습니다.</h2>")
    public ResponseEntity<?> updateBoard(@AuthenticationPrincipal int id,
                                         @PathVariable int boardId,
                                         @RequestPart(required = false) List<MultipartFile> files,
                                         @RequestPart BoardDto.CreateBoardDto dto)
    {
        boardService.checkBoardUser(id,boardId);
        if (files != null) {
            fileEntityService.saveBoardFile(files, boardId, id,"BOARD");
        }
        boardService.boardUpdate(boardId,BoardDto.CreateBoardDto.dtoToEntity(dto));
        return ResponseEntity.noContent().build();
    }

    /**
     *
     * @param id 로그인한 사용자
     * @param boardId 게시글 번호
     * @return 204 NO CONTENT로 반환
     */
    @DeleteMapping("/{boardId}")
    @ApiOperation(value = "게시글 삭제 컨트롤러",notes = "<h2>게시글을 삭제할 수 있습니다.</h2>")
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal int id, @PathVariable int boardId) {
        boardService.checkBoardUser(id,boardId);
        boardService.boardDelete(boardId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{boardId}")
    @ApiOperation(value = "댓글 등록 컨트롤러",notes = "<h2>게시글에 댓글을 등록 할 수 있습니다.</h2>")
    public ResponseEntity<?> createComment(@AuthenticationPrincipal int id, @RequestBody BoardDto.CommentDto dto, @PathVariable int boardId) {
        Board comment = boardService.createComment(BoardDto.CommentDto.dtoToEntity(dto), boardId, id);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(comment.getId()
                ).toUri()).body(ResponseWrapper.addObject(comment,HttpStatus.CREATED));
    }

    @PatchMapping("/{boardId}/{commentId}")
    @ApiOperation(value = "댓글 수정 컨트롤러",notes = "<h2>작성한 댓글을 수정할 수 있습니다.</h2>")
    public ResponseEntity<?> updateComment(@AuthenticationPrincipal int id, @PathVariable int boardId, @PathVariable int commentId, @RequestBody BoardDto.CommentDto.CommentUpdateDto dto) {
        boardService.updateComment(dto.getComment(),commentId);
        return ResponseEntity.noContent().build();
    }

    @Admin
    @PatchMapping("/auth")
    @ApiOperation(value = "게시글 숨김여부 컨트롤러",notes = "<h2>관리자가 게시글 숨김여부를 결정할 수 있습니다.</h2>")
    public ResponseEntity<?> changeViewMode(@AuthenticationPrincipal int id, @RequestBody BoardDto.ListBoardId dto) {
        boardService.changeViewMode(dto.getBoardId());
        return ResponseEntity.noContent().build();
    }

    @Admin
    @GetMapping("/admin")
    @ApiOperation(value = "관리자 전용 게시글 목록 컨트롤러", response = BoardDto.ListResponseDto.class,
    notes = "<h2>관리자 계정으로 숨김처리까지 되어있는 게시글을 볼 수 있습니다.</h2>")
    public ResponseEntity<?> adminGetBoardList(@AuthenticationPrincipal int id,
                                               @RequestParam String type,
                                               @RequestParam(required = false) String author,
                                               @RequestParam(required = false) String title
    ) {
        return ResponseEntity.ok().body(
                ResponseWrapper.addObject(
                        BoardDto.ListResponseDto.entityToDto(boardService.boardRead(type, title, author)
                        ),
                        HttpStatus.OK));
    }

    @GetMapping("/download/{fileId}")
    @ApiOperation(value = "첨부파일 다운로드 컨트롤러",notes = "<h2>게시글에 첨부된 파일을 다운로드 할 수 있습니다.</h2>")
    public ResponseEntity<?> boardAttachedFileDownload(@AuthenticationPrincipal int id, @PathVariable int fileId) {
        Map<String, Object> map = fileEntityService.boardAttachedFileDownload(fileId);
        String name = (String) map.get("fileName");
        File file = (File) map.get("file");
        Resource resource = (Resource) map.get("resource");

        String fileName = new String(name.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+fileName+"\";")
                .header(HttpHeaders.CONTENT_LENGTH,String.valueOf(file.length()))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM.toString())
                .body(resource);
    }

    @PostMapping("/image")
    @ApiOperation(value = "게시글 작성 중 이미지 추가 컨트롤러",notes = "<h2>게시글 작성 중 이미지를 추가하는 기능을 수행합니다.</h2>")
    public ResponseEntity<?> boardImageUpload(@AuthenticationPrincipal int id, @RequestParam MultipartFile file) {
        return ResponseEntity.ok().body(ResponseWrapper.addObject(fileEntityService.saveImage(file, id),HttpStatus.OK));
    }

}
