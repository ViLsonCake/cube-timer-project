package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.comparator.SolveTimeCompare;
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
    private final SolveTimeCompare solveTimeCompare;

    @Autowired
    public TimerService(UserRepository userRepository, SolveRepository solveRepository, SolveTimeCompare solveTimeCompare) {
        this.userRepository = userRepository;
        this.solveRepository = solveRepository;
        this.solveTimeCompare = solveTimeCompare;
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

    public static int toSeconds(String time) {
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

    public String getAverageOf5(String username, String cube) {
        List<SolveEntity> lastFiveSolve = solveRepository.findLastFiveSolveByUserIdAndCube(userRepository.findByUsername(username).getUserId(), cube);

        // If user have less than 5 solves
        if (lastFiveSolve.size() < 5) {
            return "--:--";
        }

        int totalTimeInMillis = 0;

        for (SolveEntity solve : lastFiveSolve) {
            totalTimeInMillis += toSeconds(solve.getTime());
        }
        // Subtract the best and words result
//        totalTimeInMillis -= getMaxTime(lastFiveSolve) - getMinTime(lastFiveSolve);
        totalTimeInMillis -= (toSeconds(lastFiveSolve.stream().max(solveTimeCompare).get().getTime()) - toSeconds(lastFiveSolve.stream().min(solveTimeCompare).get().getTime()));

        // Get arithmetic mean
        totalTimeInMillis /= 3;

        return convertToTime(totalTimeInMillis);
    }

    public String getAverageOf12(String username, String cube) {
        List<SolveEntity> lastFiveSolve = solveRepository.findLastTwelveSolveByUserIdAndCube(userRepository.findByUsername(username).getUserId(), cube);

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

    public String getLastTime(String username, String cube) {
        // Get last user solve in table
        SolveEntity solveEntity = solveRepository.findLastSolveByUserIdAndCube(userRepository.findByUsername(username).getUserId(), cube);

        // If user don't have solve show empty timer
        if (solveEntity == null)
            return "00:00";

        return solveEntity.getTime();
    }

    public void timerPage(Model model, String cube, String username) throws IOException, CubeNotValidException {
        // Add attributes
        model.addAttribute("cube", cube);
        model.addAttribute("username", username);
        model.addAttribute("lastSolveTime", getLastTime(username, cube));
        model.addAttribute("averageOf5", getAverageOf5(username, cube));
        model.addAttribute("averageOf12", getAverageOf12(username, cube));
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
