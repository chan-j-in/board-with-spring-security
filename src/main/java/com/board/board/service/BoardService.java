package com.board.board.service;

import com.board.board.dto.BoardForm;
import com.board.board.entity.Board;
import com.board.board.entity.User;
import com.board.board.exception.DataNotFoundException;
import com.board.board.entity.FileEntity;
import com.board.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final FileService fileService;

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
            board.increaseHitCount();
            boardRepository.save(board);
        }
    }

    public void create (BoardForm boardForm, User user, List<MultipartFile> files) {
        List<FileEntity> fileEntityList = new ArrayList<>();
        Board board = Board.builder()
                .subject(boardForm.getSubject())
                .content(boardForm.getContent())
                .regDate(LocalDateTime.now())
                .hit(0)
                .user(user)
                .fileEntityList(fileEntityList)
                .build();
        boardRepository.save(board);

        try {
            for (MultipartFile file : files) {
                fileEntityList.add(fileService.saveFile(file, board));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Specification<Board> search(String kw) {
    }

}