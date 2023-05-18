package com.webkit640.backend.service.impl;

import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.BoardRepository;
import com.webkit640.backend.repository.MemberRepository;
import com.webkit640.backend.service.logic.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, MemberRepository memberRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public Board createBoard(Board board,int memberId) {
        Member member = memberRepository.findById(memberId);
        List<Board> boards = member.getBoards();
        boards.add(board);
        board.setMember(member);
        Board save = boardRepository.save(board);
        memberRepository.save(member);
        return save;
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
