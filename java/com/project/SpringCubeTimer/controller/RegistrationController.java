package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.entity.UserEntity;
import com.project.SpringCubeTimer.service.RegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {

    private final RegistrationService registrationService;

    @Autowired
    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @GetMapping("/registration")
    public String showRegisterPage(Model model) {
        return "registration";
    }

    @PostMapping("/registration")
    public String registrationUser(@RequestParam @Valid String username,
                                   @RequestParam @Valid String email,
                                   @RequestParam @Valid String password,
                                   @RequestParam String confirmPassword,
                                   HttpServletResponse response,
                                   Model model) {
        return registrationService.register(new UserEntity(username.trim(), email.trim(), password.trim()),
                confirmPassword, response, model);

    }

}
