package com.project.SpringCubeTimer.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;

@Service
public class TimerService {

    public String getRandomScramble(String cube) throws IOException {
        String url = "https://rubiks-cube-scramble-generator.onrender.com/api?puzzleType=" + cube;

        Document document = Jsoup.connect(url).ignoreContentType(true).get();

        return document.text().substring(13, document.text().length() - 2);
    }

    public void timerPage(Model model) throws IOException {
        model.addAttribute("scramble", getRandomScramble("3x3"));
    }
}
