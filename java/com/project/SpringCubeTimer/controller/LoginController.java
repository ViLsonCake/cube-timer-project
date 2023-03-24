package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "login-page";
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam @Valid String username,
                            @RequestParam @Valid String password,
                            Model model, HttpServletResponse response) {

        loginService.makeLogged(response, username);

        return "redirect:/timer/3x3";

    }
}
