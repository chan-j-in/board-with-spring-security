package com.board.board.controller;

import com.board.board.dto.UserCreateForm;
import com.board.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/signup")
    public String signup(UserCreateForm userCreateForm) {
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(Model model, UserCreateForm userCreateForm, BindingResult bindingResult) {

        if (userCreateForm.getUsername().isEmpty() || userCreateForm.getEmail().isEmpty() || userCreateForm.getPassword1().isEmpty()) {
            System.out.println("회원가입 폼은 전체 필수 입력 값입니다.");
            return "signup";
        }

        if (!userCreateForm.getPassword1().equals(userCreateForm.getPassword2())) {
            System.out.println("패스워드가 일치하지 않습니다.");
            return "signup";
        }

        try {
            userService.create(userCreateForm.getUsername(), userCreateForm.getEmail(), userCreateForm.getPassword1());
        } catch (DataIntegrityViolationException e) {
            System.out.println("이미 등록된 사용자 입니다.");
            bindingResult.reject("alreadyUser", "이미 등록된 사용자 입니다.");
            e.printStackTrace();
            return "login";
        } catch (Exception e) {
            e.printStackTrace();
            return "signup";
        }

        return "redirect:/user/login";
    }

}
