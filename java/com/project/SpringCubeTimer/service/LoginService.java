package com.project.SpringCubeTimer.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

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
}
