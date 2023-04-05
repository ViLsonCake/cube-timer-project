package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.exception.CubeNotValidException;
import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.validate.RequestBodyValidation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;

@Service
public class TimerService {

    private final UserRepository userRepository;
    private final SolveRepository solveRepository;

    @Autowired
    public TimerService(UserRepository userRepository, SolveRepository solveRepository) {
        this.userRepository = userRepository;
        this.solveRepository = solveRepository;
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

    public void saveSolve(String username, String body) {

        // Remove first and last character
        String output = body.substring(1, body.length() - 1);

        // Split the request output
        String[] data = output.split(",");

        if (RequestBodyValidation.isValidTime(data[1]) && RequestBodyValidation.isValidCube(data[2])) {
            // Create output solve entity if request body is valid
            SolveEntity solve = new SolveEntity(data[0], data[1], data[2], userRepository.findByUsername(username));

            // Save solve
            solveRepository.save(solve);
        }

    }

}
