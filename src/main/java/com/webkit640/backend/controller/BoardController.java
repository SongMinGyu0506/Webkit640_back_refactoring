package com.webkit640.backend.controller;

import com.webkit640.backend.dto.BoardDto;
import com.webkit640.backend.dto.response.ResponseWrapper;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.service.logic.BoardService;
import com.webkit640.backend.service.logic.FileEntityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

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
    public ResponseEntity<?> getListBoard(@AuthenticationPrincipal int id,
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

    /**
     * 게시글을 작성하는 컨트롤러
     * @param id 로그인 사용자
     * @param files 클라이언트에서 업로드하는 파일 데이터
     * @param dto 게시글 양식에 따라 작성한 텍스트 데이터
     * @return Response Header에 데이터가 저장된 URI를 반환하고, 작성된 데이터를 body에 간단하게 작성 <br/>
     * HTTP STATUS: 201
     */
    @PostMapping("")
    public ResponseEntity<?> createBoard(@AuthenticationPrincipal int id, @RequestPart(required = false) List<MultipartFile> files, @RequestPart BoardDto.CreateBoardDto dto) {
        Board board = boardService.createBoard(BoardDto.CreateBoardDto.dtoToEntity(dto),id);
        List<FileEntity> fileEntities = fileEntityService.saveBoardFile(files, board.getId(), id);

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
    public ResponseEntity<?> updateBoard(@AuthenticationPrincipal int id,
                                         @PathVariable int boardId,
                                         @RequestPart(required = false) List<MultipartFile> files,
                                         @RequestPart BoardDto.CreateBoardDto dto)
    {
        boardService.checkBoardUser(id,boardId);
        if (files != null) {
            fileEntityService.saveBoardFile(files, boardId, id);
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
    public ResponseEntity<?> deleteBoard(@AuthenticationPrincipal int id, @PathVariable int boardId) {
        boardService.checkBoardUser(id,boardId);
        boardService.boardDelete(boardId);
        return ResponseEntity.noContent().build();
    }

}
