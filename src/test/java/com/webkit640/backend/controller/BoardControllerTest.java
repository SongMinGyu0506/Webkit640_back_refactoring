package com.webkit640.backend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.repository.repository.FileEntityRepository;
import com.webkit640.backend.service.logic.BoardService;
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

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
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
    FileEntityRepository fileEntityRepository;

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
}