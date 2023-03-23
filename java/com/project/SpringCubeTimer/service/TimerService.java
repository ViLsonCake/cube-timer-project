package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.exception.CubeNotValidException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;

@Service
public class TimerService {

    public boolean isValidCube(String cube) {
        return switch (cube.toLowerCase()) {
            case "2x2", "3x3", "4x4", "5x5", "6x6", "7x7", "pyraminx", "megaminx", "square-1", "clock" -> true;
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

    public void timerPage(Model model, String cube) throws IOException, CubeNotValidException {
        model.addAttribute("cube", cube);
        model.addAttribute("scramble", getRandomScramble(cube));
    }
}
