package com.project.SpringCubeTimer.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    public void makeLogged(HttpServletResponse response) {
        Cookie cookie = new Cookie("isLogged", "true");
        cookie.setMaxAge(toSeconds(7));

        response.addCookie(cookie);
    }

    public int toSeconds(int days) {
        return days * 24 * 60 * 60;
    }
}
