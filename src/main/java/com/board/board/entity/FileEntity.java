package com.board.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "file")
public class FileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String origNm;

    private String saveNm;

    private String savedPath;

    @ManyToOne
    @JoinColumn(name = "file_id")
    private Board board;

    @Builder
    public FileEntity(String origNm, String saveNm, String savedPath, Board board) {
        this.origNm = origNm;
        this.saveNm = saveNm;
        this.savedPath = savedPath;
        this.board = board;
    }
}
