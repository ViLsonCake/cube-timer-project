package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.entity.UserEntity;
import com.project.SpringCubeTimer.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    private final LoginService loginService;

    @Autowired
    public RegistrationService(UserRepository userRepository, LoginService loginService) {
        this.userRepository = userRepository;
        this.loginService = loginService;
    }

    public String register(UserEntity user, HttpServletResponse response, Model model) {

        if (userRepository.findByUsername(user.getUsername()) != null ||
                userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("UserExistError", "User already exist");
            return "registration-page";
        }

        if (user.getUsername().equals("UNKNOWN")) {
            model.addAttribute("invalidName", "Name is not valid");
            return "registration";
        }

        // Save user to db and cookie session
        userRepository.save(user);
        loginService.makeLogged(response, user.getUsername());

        return "redirect:/timer/3x3";

    }
}
