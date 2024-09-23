package com.board.board.repository;

import com.board.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Query(value =
            "select * from board " +
                    "where reg_date > (select reg_date from board where id = :id) " +
                    "limit 1", nativeQuery = true)
    Optional<Board> findByNextBoard(Integer id);

    @Query(value =
            "select * from board " +
                    "where reg_date > (select reg_date from board where id = :id) order by reg_date desc " +
                    "limit 1", nativeQuery = true)
    Optional<Board> findByPrevBoard(Integer id);

    Page<Board> findAll(Pageable pageable);

    Page<Board> findAll(Specification<Board> spec, Pageable pageable);

    Page<Board> findBySubjectContaining(String keyword, Pageable pageable);

    Page<Board> findByContentContaining(String keyword, Pageable pageable);

    Page<Board> findBySubjectOrContentContaining(String keyword1, String keyword2, Pageable pageable);

}
