package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Board;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public interface BoardService {
    Board createBoard(Board board,int memberId);
    List<Board> boardRead(String type, String title, String author);
    Board boardRead(int id);
    Board boardUpdate(int id, Board updateBoard);
    Board boardDelete(int id);
    List<Board> replyRead(int id);

}
