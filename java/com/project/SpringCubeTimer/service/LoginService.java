package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.service.serviceConst.ServiceConst;
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

    public static void makeLogged(HttpServletResponse response, String username) {
        Cookie usernameCookie = new Cookie("username", username);
        usernameCookie.setMaxAge(toSeconds(90));

        response.addCookie(usernameCookie);
    }

    public static int toSeconds(int days) {
        return days * 24 * 60 * 60;
    }

    public boolean isLogged(String username) {
        return !username.equals("UNKNOWN");
    }

    // Get request
    public String getLoginPage(String username) {
        if (isLogged(username)) return "redirect:/timer/3x3";
        return "login.html";
    }

    // Post request
    public String loginUser(String username, String password, HttpServletResponse response, Model model) {
        // If user not found or password is incorrect
        if (userRepository.findByUsername(username) == null ||
                (!userRepository.findByUsername(username).getPassword().equals(password))) {
            model.addAttribute("loginError", ServiceConst.LOGIN_MESSAGE);
            return "login.html";
        }

        // Add user to cookie session
        makeLogged(response, username);

        return "redirect:/timer/3x3";
    }

    public void logoutUser(HttpServletResponse response) {
        Cookie usernameCookie = new Cookie("username", "UNKNOWN");
        usernameCookie.setMaxAge(0);

        response.addCookie(usernameCookie);
    }
}
