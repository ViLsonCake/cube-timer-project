package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.entity.UserEntity;
import com.project.SpringCubeTimer.exception.CubeNotValidException;
import com.project.SpringCubeTimer.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.io.IOException;

@Service
public class TimerService {

    private UserRepository userRepository;

    @Autowired
    public TimerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isValidCube(String cube) {
        return switch (cube.toLowerCase()) {
            case "2x2", "3x3", "4x4", "5x5", "6x6", "7x7" -> true;
            default -> false;
        };
    }

    public String getRandomScramble(String cube) throws IOException, CubeNotValidException {
        if (!isValidCube(cube))
            throw new CubeNotValidException("Cube not valid");

        String url = "https://rubiks-cube-scramble-generator.onrender.com/api?puzzleType=" + cube;

        Document document = Jsoup.connect(url).ignoreContentType(true).get();

        return document.text().substring(13, document.text().length() - 2);
    }

    public void timerPage(Model model, String cube, String username) throws IOException, CubeNotValidException {
        model.addAttribute("cube", cube);
        model.addAttribute("username", username);
        model.addAttribute("scramble", getRandomScramble(cube));
    }



}
