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
import java.util.List;

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
            throw new CubeNotValidException("Cube is not valid");

        String url = "https://rubiks-cube-scramble-generator.onrender.com/api?puzzleType=" + cube;

        Document document = Jsoup.connect(url).ignoreContentType(true).get();

        return document.text().substring(13, document.text().length() - 2);
    }

    public int getMaxTime(Iterable<SolveEntity> solves) {
        int largest = 0;

        for (SolveEntity solve : solves) {
            if (toSeconds(solve.getTime()) > largest)
                largest = toSeconds(solve.getTime());
        }

        return largest;
    }

    public int getMinTime(Iterable<SolveEntity> solves) {
        int min = 0;

        for (SolveEntity solve : solves) {
            if (toSeconds(solve.getTime()) < min)
                min = toSeconds(solve.getTime());
        }

        return min;
    }

    public int toSeconds(String time) {
        // Incorrect input
        if (!time.contains(":"))
            return -1;

        String[] minutesAndSeconds = time.split(":");

        // Format seconds:millis
        if (minutesAndSeconds.length == 2) {
            int seconds = Integer.parseInt(minutesAndSeconds[0]);
            int mills = Integer.parseInt(minutesAndSeconds[1]);

            return seconds * 100 + mills;
        // Format minutes:seconds:mills
        } else if (minutesAndSeconds.length == 3) {
            int minutes = Integer.parseInt(minutesAndSeconds[0]);
            int seconds = Integer.parseInt(minutesAndSeconds[1]);
            int mills = Integer.parseInt(minutesAndSeconds[2]);

            return minutes * 60 * 100 + seconds * 100 + mills;
        }

        return -1;
    }

    public String convertToTime(int timeInMillis) {
        int minutes = timeInMillis / (60 * 100);
        int seconds = (timeInMillis / 100) % 60;
        int millis = timeInMillis % 100;

        if (minutes == 0) {
            if (seconds < 10 && millis < 10)
                return "0" + seconds + ":0" + millis;
            else if (seconds < 10 && millis >= 10)
                return "0" + seconds + ":" + millis;
            else if (seconds >= 10 && millis < 10)
                return seconds + ":0" + millis;
            else if (seconds >= 10 && millis >= 10)
                return seconds + ":" + millis;
        } else {
            if (minutes < 10) {
                if (seconds < 10 && millis < 10)
                    return "0" + minutes + ":0" + seconds + ":0" + millis;
                else if (seconds < 10 && millis > 10)
                    return "0" + minutes + ":0" + seconds + ":" + millis;
                else if (seconds > 10 && millis < 10)
                    return "0" + minutes + ":" + seconds + ":0" + millis;
                else if (seconds > 10 && millis > 10)
                    return "0" + minutes + ":" + seconds + ":" + millis;
            } else {
                if (seconds < 10 && millis < 10)
                    return minutes + ":0" + seconds + ":0" + millis;
                else if (seconds < 10 && millis > 10)
                    return minutes + ":0" + seconds + ":" + millis;
                else if (seconds > 10 && millis < 10)
                    return minutes + ":" + seconds + ":0" + millis;
                else if (seconds > 10 && millis > 10)
                    return minutes + ":" + seconds + ":" + millis;
            }
        }

        return "";

    }

    public String getAverageOf5(String username) {
        List<SolveEntity> lastFiveSolve = solveRepository.getLastFiveSolveByUser_id(userRepository.findByUsername(username).getUserId());

        // If user have less than 5 solves
        if (lastFiveSolve.size() < 5) {
            return "--:--";
        }

        int totalTimeInMillis = 0;

        for (SolveEntity solve : lastFiveSolve) {
            totalTimeInMillis += toSeconds(solve.getTime());
        }
        // Subtract the best and words result
        totalTimeInMillis -= getMaxTime(lastFiveSolve) - getMinTime(lastFiveSolve);

        // Get arithmetic mean
        totalTimeInMillis /= 3;

        return convertToTime(totalTimeInMillis);
    }

    public String getAverageOf12(String username) {
        List<SolveEntity> lastFiveSolve = solveRepository.getLastTwelveSolveByUser_id(userRepository.findByUsername(username).getUserId());

        // If user have less than 12 solves
        if (lastFiveSolve.size() < 12) {
            return "--:--";
        }

        int totalTimeInMillis = 0;

        for (SolveEntity solve : lastFiveSolve) {
            totalTimeInMillis += toSeconds(solve.getTime());
        }
        // Subtract the best and words result
        totalTimeInMillis -= getMaxTime(lastFiveSolve) - getMinTime(lastFiveSolve);

        // Get arithmetic mean
        totalTimeInMillis /= 10;

        return convertToTime(totalTimeInMillis);
    }

    public void timerPage(Model model, String cube, String username) throws IOException, CubeNotValidException {
        // Get last user solve in table
        String time = solveRepository.findLastSolveByUserId(userRepository.findByUsername(username).getUserId()).getTime();

        // If user don't have solve show empty timer
        if (time == null)
            time = "00:00";

        // Add attributes
        model.addAttribute("cube", cube);
        model.addAttribute("username", username);
        model.addAttribute("lastSolveTime", time);
        model.addAttribute("averageOf5", getAverageOf5(username));
        model.addAttribute("averageOf12", getAverageOf12(username));
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
