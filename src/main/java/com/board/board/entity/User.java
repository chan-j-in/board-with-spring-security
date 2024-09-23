package com.board.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Column(unique = true)
    private String email;

    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Board> boardList = new ArrayList<>();

    @Builder
    public User(String username, String password, String email, LocalDateTime createdDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdDate = createdDate;
    }
}
