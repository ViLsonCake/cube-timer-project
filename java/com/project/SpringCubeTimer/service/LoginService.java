package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class LoginService {
    private final UserRepository userRepository;

    @Autowired
    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void makeLogged(HttpServletResponse response, String username) {
        Cookie authenticationCookie = new Cookie("isLogged", "true");
        Cookie usernameCookie = new Cookie("username", username);

        authenticationCookie.setMaxAge(toSeconds(7));

        response.addCookie(authenticationCookie);
        response.addCookie(usernameCookie);
    }

    public int toSeconds(int days) {
        return days * 24 * 60 * 60;
    }

    public String loginUser(String username, String password, HttpServletResponse response, Model model) {
        // If user not found or password is incorrect
        if (userRepository.findByUsername(username) == null ||
                (!userRepository.findByUsername(username).getPassword().equals(password))) {
            model.addAttribute("loginError", "Incorrect login or password");
            return "login-page";
        }

        // Add user to cookie session
        makeLogged(response, username);

        return "redirect:/timer/3x3";
    }
}
