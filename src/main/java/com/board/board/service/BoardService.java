package com.board.board.service;

import com.board.board.entity.Board;
import com.board.board.exception.DataNotFoundException;
import com.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;

    public Page<Board> boardList(String query, String kw, int page) {

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("regDate"));

        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));

        if (query.equals("subject")) {
            return boardRepository.findBySubjectContaining(kw, pageable);
        } else if (query.equals("content")) {
            return boardRepository.findByContentContaining(kw, pageable);
        } else if (query.equals("user")) {
            Specification<Board> spec = search(kw);
            return boardRepository.findAll(spec, pageable);
        } else if (query.equals("subject+content")) {
            return boardRepository.findBySubjectOrContentContaining(kw, kw, pageable);
        } else {
            return boardRepository.findAll(pageable);
        }
    }

    public Board detail (Integer boardId) {
        Optional<Board> _board = boardRepository.findById(boardId);
        if(_board.isPresent())
            return _board.get();
        throw new DataNotFoundException("board is empty");
    }

    public Board nextPost(Integer boardId) {
        Optional<Board> _nextPost = boardRepository.findByNextBoard(boardId);
        return _nextPost.orElseGet(Board::new);
    }

    public Board prevPost(Integer boardId) {
        Optional<Board> _prevPost = boardRepository.findByPrevBoard(boardId);
        return _prevPost.orElseGet(Board::new);
    }

    public void hitUpdate (Integer boardId) {
        Optional<Board> _board = boardRepository.findById(boardId);
        if(_board.isPresent()) {
            Board board = _board.get();
            board.setHit(board.getHit() + 1);
            boardRepository.save(board);
        }
    }

    private Specification<Board> search(String kw) {
    }

}