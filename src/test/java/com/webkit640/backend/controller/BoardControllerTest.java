package com.webkit640.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit640.backend.dto.BoardDto;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.FileEntity;
import com.webkit640.backend.repository.repository.BoardRepository;
import com.webkit640.backend.repository.repository.FileEntityRepository;
import com.webkit640.backend.service.logic.BoardService;
import com.webkit640.backend.service.logic.FileEntityService;
import org.apache.tika.metadata.HttpHeaders;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MimeTypeUtils;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Commit
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class BoardControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MockMvc mockMvc;

    @Autowired
    RepositoryControl repositoryControl;

    @Autowired
    BoardService boardService;
    @Autowired
    FileEntityService fileEntityService;

    @Autowired
    FileEntityRepository fileEntityRepository;
    @Autowired
    BoardRepository boardRepository;

    int counter;
    @AfterEach
    void init() {
        repositoryControl.init();
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("게시글 상세보기 테스트")
    void getBoard() throws Exception {
        createBoard();
        System.out.println(counter);
        mockMvc.perform(MockMvcRequestBuilders.get("/board/"+counter)).andExpect((status().isOk())).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("게시글 리스트 테스트")
    void getListBoard() throws Exception {
        for (int i = 0; i < 3; i++) {
            counter++;
            createBoard();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/board?type=TEST")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("게시글 생성")
    void createBoard() throws Exception {
        counter++;
        Map<String,String> map = new HashMap<>();
        map.put("type","TEST");
        map.put("title","title");
        map.put("author","author");
        map.put("content","content");
        String value = objectMapper.writeValueAsString(map);
        System.out.println(value);
        MockMultipartFile files = new MockMultipartFile(
                "files",
                "test.pdf",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "Hello,World!".getBytes()
        );
        MockMultipartFile dto = new MockMultipartFile(
                "dto",
                "dto",
                "application/json",
                value.getBytes(StandardCharsets.UTF_8)
        );
        mockMvc.perform(
                multipart("/board")
                        .file(files)
                        .file(dto)
        ).andExpect(status().isCreated()).andDo(print());

    }
    @Test
    @WithAccount("test@test.com")
    @DisplayName("게시글 수정")
    void updateBoard() throws Exception {
        createBoard();

        Map<String,String> map = new HashMap<>();
        map.put("type","TESTss");
        map.put("title","titless");
        map.put("author","authorss");
        map.put("content","contentss");
        String value = objectMapper.writeValueAsString(map);


        MockMultipartFile files1 = new MockMultipartFile(
                "files",
                "testasd.pdf",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "Hello,Worldsss!".getBytes()
        );
        MockMultipartFile files2 = new MockMultipartFile(
                "files",
                "test.pdf",
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                "Hello,World!".getBytes()
        );

        MockMultipartFile dto = new MockMultipartFile(
                "dto",
                "dto",
                "application/json",
                value.getBytes(StandardCharsets.UTF_8)
        );
        mockMvc.perform(multipart(HttpMethod.PATCH,"/board/1").file(files1).file(files2).file(dto)).andExpect(status().isNoContent()).andDo(print());
        fileEntityRepository.findByBoardId(1).forEach(file -> System.out.println(file.getFileName()));
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("게시글 제거")
    void deleteBoard() throws Exception {
        createBoard();
        mockMvc.perform(MockMvcRequestBuilders.delete("/board/1")).andExpect(status().isNoContent()).andDo(print());
        assertAll(
                ()->assertThat(boardService.boardRead("TEST", null, null,false).size()).isEqualTo(0)
        );
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("댓글 생성")
    void createComment() throws Exception{
        createBoard();
        BoardDto.CommentDto dto = BoardDto.CommentDto.builder()
                .comment("test comment1")
                .boardId(1)
                .build();
        BoardDto.CommentDto dtos = BoardDto.CommentDto.builder()
                .comment("test comment2")
                .boardId(1)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/board/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated()).andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.post("/board/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dtos)))
                .andExpect(status().isCreated()).andDo(print());



        List<Board> boards = boardRepository.findById(1).getBoards();
        boards.forEach(comment -> assertAll(
                ()->assertThat(comment.getContent()).contains("test comment")
        ));
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("관리자 게시글 열람권한 변경")
    void changeViewMode() throws Exception {
        createBoard();
        createComment();

        List<Integer> raw = new ArrayList<>();
        raw.add(1);
        raw.add(2);
        BoardDto.ListBoardId dto = BoardDto.ListBoardId.builder().boardId(raw).build();

        mockMvc.perform(MockMvcRequestBuilders.get("/board?type=TEST")).andExpect(status().isOk()).andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.patch("/board/auth").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent()).andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.get("/board?type=TEST")).andExpect(status().isOk()).andDo(print());
        mockMvc.perform(MockMvcRequestBuilders.get("/board/1")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("업로드 파일 다운로드 테스트")
    void attachedFileDownload() throws Exception {
        createBoard();
        mockMvc.perform(MockMvcRequestBuilders.get("/board/download/1").contentType(MediaType.APPLICATION_OCTET_STREAM)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("댓글 수정 테스트")
    void updateComment() throws Exception {
        createComment();

        BoardDto.CommentDto.CommentUpdateDto build = BoardDto.CommentDto.CommentUpdateDto.builder().comment("This is Update Comment!!!").build();

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/board/1/2").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(build)))
                .andExpect(status().isNoContent()).andDo(print());

        mockMvc.perform(MockMvcRequestBuilders.get("/board/1")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("게시글 작성 중 이미지 업로드 테스트")
    void imageUploadTest() throws Exception {
        File imageFile = new File("C:\\Users\\song\\Desktop\\test.png");
        FileInputStream inputStream = new FileInputStream(imageFile);

        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",       // 파일 이름
                "image.jpg",       // 원본 파일 이름
                MimeTypeUtils.IMAGE_JPEG_VALUE,  // 파일 타입
                inputStream        // 파일 데이터
        );
        String image = fileEntityService.saveImage(mockMultipartFile, 1);
        fileEntityRepository.findAll().forEach(file -> {
            System.out.println(file.getFileName());
        });
        System.out.println(image);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/board/image").file(mockMultipartFile)).andExpect(status().isOk()).andDo(print());
    }
}