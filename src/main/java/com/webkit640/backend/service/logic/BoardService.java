package com.webkit640.backend.service.logic;

import com.webkit640.backend.entity.Board;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BoardService {
    Board createBoard(Board board,int memberId);
    List<Board> boardRead(String type, String title, String author, boolean isAdd);
    List<Board> boardRead(String type, String title, String author);
    Board boardRead(int id);
    Board boardUpdate(int id, Board updateBoard);
    void boardDelete(int id);
    List<Board> replyRead(int id);
    void checkBoardUser(int memberId, int boardId);
    Board createComment(Board board,int boardId, int memberId);
    void changeViewMode(List<Integer> boardIds);
    void updateComment(String comment, int commentId);
}
