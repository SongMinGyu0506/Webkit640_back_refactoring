package com.webkit640.backend.service.impl;

import com.webkit640.backend.entity.Board;
import com.webkit640.backend.service.logic.BoardService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class BoardServiceImpl implements BoardService {
    @Override
    public Board create(String type, Board board) {
        return null;
    }

    @Override
    public List<Board> boardRead(String type, String title, String author) {
        return null;
    }

    @Override
    public Board boardRead(int id) {
        return null;
    }

    @Override
    public Board boardUpdate(int id, HashMap<String, String> updateData) {
        return null;
    }

    @Override
    public Board boardDelete(int id) {
        return null;
    }

    @Override
    public List<Board> replyRead(int id) {
        return null;
    }
}
