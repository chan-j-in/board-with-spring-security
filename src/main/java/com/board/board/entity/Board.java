package com.board.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String subject;

    @Lob
    private String content;

    private LocalDateTime regDate;

    private int hit;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<FileEntity> fileEntityList = new ArrayList<>();

    @Builder
    public Board(String subject, String content, LocalDateTime regDate, int hit, User user, List<FileEntity> fileEntityList) {
        this.subject = subject;
        this.content = content;
        this.regDate = regDate;
        this.hit = hit;
        this.user = user;
        this.fileEntityList = fileEntityList;
    }

    public void increaseHitCount() {
        this.hit++;
    }
}
