package com.board.board.controller;

import com.board.board.dto.BoardForm;
import com.board.board.entity.Board;
import com.board.board.entity.FileEntity;
import com.board.board.entity.User;
import com.board.board.repository.FileRepository;
import com.board.board.service.BoardService;
import com.board.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final UserService userService;
    private final FileRepository fileRepository;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(BoardForm boardForm) {
        return "board";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(BoardForm boardForm,
                         @RequestParam("files") List<MultipartFile> files,
                         Principal principal) {
        User user = userService.getUser(principal.getName());
        boardService.create(boardForm, user, files);
        return "redirect:/board/list";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<UrlResource> download(@PathVariable("id") Long id) throws MalformedURLException {
        FileEntity file = fileRepository.findById(id).orElse(null);
        UrlResource resource = new UrlResource("file:" + file.getSavedPath());
        String encodedFileName = UriUtils.encode(file.getOrigNm(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }
}
