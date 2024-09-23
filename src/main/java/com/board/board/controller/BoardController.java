package com.board.board.controller;

import com.board.board.entity.Board;
import com.board.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "query", defaultValue = "subject") String query,
                       @RequestParam(value = "kw", defaultValue = "") String kw,
                       @RequestParam(value = "page", defaultValue = "0") int page) {

        Page<Board> boardList = boardService.boardList(query, kw, page);
        model.addAttribute("list", boardList);
        model.addAttribute("kw", kw);
        model.addAttribute("query", query);
        return "list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id) {

        boardService.hitUpdate(id);
        Board post = boardService.detail(id);
        Board nextPost = boardService.nextPost(id);
        Board prevPost = boardService.prevPost(id);

        model.addAttribute("post", post);
        model.addAttribute("nextPost", nextPost);
        model.addAttribute("prevPost", prevPost);
        return "detail";
    }
}
