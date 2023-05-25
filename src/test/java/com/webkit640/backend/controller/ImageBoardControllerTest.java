package com.webkit640.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webkit640.backend.dto.ImageBoardDto;
import com.webkit640.backend.service.logic.ImageBoardService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.MimeTypeUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Commit
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ImageBoardControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RepositoryControl repositoryControl;

    @Autowired
    ImageBoardService imageBoardService;

    @Test
    @WithAccount("test@test.com")
    @DisplayName("이미지 게시판 목록 리스트 테스트")
    void getImageBoardList() throws Exception {
        addImageBoard();
        addImageBoard();
        addImageBoard();
        mockMvc.perform(MockMvcRequestBuilders.get("/image")).andExpect(status().isOk()).andDo(print());

    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("이미지 게시판 상세 테스트")
    void getImageBoard() throws Exception {
        addImageBoard();
        mockMvc.perform(MockMvcRequestBuilders.get("/image/1")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("이미지 게시판 게시글 추가 테스트")
    void addImageBoard() throws Exception {
        File imageFile = new File("C:\\Users\\song\\Desktop\\test.png");
        FileInputStream inputStream = new FileInputStream(imageFile);

        HashMap<String,String> raw = new HashMap<String,String>();
        raw.put("title","TEST");
        String s = objectMapper.writeValueAsString(raw);

        System.out.println(s);
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "image",       // 파일 이름
                "image.jpg",       // 원본 파일 이름
                MimeTypeUtils.IMAGE_JPEG_VALUE,  // 파일 타입
                inputStream        // 파일 데이터
        );

        MockMultipartFile dto = new MockMultipartFile(
                "dto",
                "dto",
                "application/json",
                s.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/image").file(mockMultipartFile).file(dto))
                .andExpect(status().isCreated()).andDo(print());
    }

    @Test
    @WithAccount("test@test.com")
    @DisplayName("이미지 게시판 삭제 테스트")
    void deleteImageBoard() throws Exception {
        addImageBoard();
        mockMvc.perform(MockMvcRequestBuilders.delete("/image/1")).andExpect(status().isNoContent()).andDo(print());
    }
}