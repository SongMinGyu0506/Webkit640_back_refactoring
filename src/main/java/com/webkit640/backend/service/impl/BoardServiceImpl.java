package com.webkit640.backend.service.impl;

import com.webkit640.backend.config.exception.NoAuthenticationUserException;
import com.webkit640.backend.entity.Board;
import com.webkit640.backend.entity.Member;
import com.webkit640.backend.repository.repository.BoardRepository;
import com.webkit640.backend.repository.repository.FileEntityRepository;
import com.webkit640.backend.repository.repository.MemberRepository;
import com.webkit640.backend.repository.spec.BoardSpec;
import com.webkit640.backend.service.logic.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final FileEntityRepository fileEntityRepository;

    @Autowired
    public BoardServiceImpl(BoardRepository boardRepository, MemberRepository memberRepository, FileEntityRepository fileEntityRepository) {
        this.boardRepository = boardRepository;
        this.memberRepository = memberRepository;
        this.fileEntityRepository = fileEntityRepository;
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
        Specification<Board> spec = (root,query,criteriaBuilder) -> null;
        spec = spec.and(BoardSpec.equalType(type));
        if (title != null) {
            spec = spec.and(BoardSpec.likeTitle(title));
        } else if (author != null) {
            spec = spec.and(BoardSpec.equalAuthor(author));
        }
        return boardRepository.findAll(spec);
    }

    @Override
    public Board boardRead(int id) {
        return boardRepository.findById(id);
    }

    @Override
    public Board boardUpdate(int id, Board updateData) {
        Board board = boardRepository.findById(id);
        board.setBoardType(updateData.getBoardType());
        board.setContent(updateData.getContent());
        board.setTitle(updateData.getTitle());
        boardRepository.save(board);
        return board;
    }

    @Override
    public void boardDelete(int id) {
        fileEntityRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }

    @Override
    public List<Board> replyRead(int id) {
        return null;
    }

    @Override
    public void checkBoardUser(int memberId, int boardId) {
        Board board = boardRepository.findById(boardId);
        if (memberId != board.getMember().getId()) {
            throw new NoAuthenticationUserException("게시글의 작성자가 아닙니다.");
        }
    }
}
