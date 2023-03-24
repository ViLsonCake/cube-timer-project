package com.project.SpringCubeTimer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String showRegisterPage(Model model) {
        return "registration-page";
    }

}
