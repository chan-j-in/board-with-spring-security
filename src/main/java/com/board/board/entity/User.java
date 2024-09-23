package com.board.board.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Builder
    public User(String username, String password, String email, LocalDateTime createdDate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdDate = createdDate;
    }
}
