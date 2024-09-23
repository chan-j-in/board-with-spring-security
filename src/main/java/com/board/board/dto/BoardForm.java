package com.board.board.dto;

import com.board.board.entity.User;
import com.board.board.entity.FileEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BoardForm {

    private String subject;
    private String content;
    private User user;
    private List<FileEntity> fileEntityList = new ArrayList<>();
}
