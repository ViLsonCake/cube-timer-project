package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.comparator.SolveTimeCompare;
import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.exception.CubeNotValidException;
import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.service.serviceConst.ServiceConst;
import com.project.SpringCubeTimer.validate.RequestBodyValidation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.List;

@Service
public class TimerService {

    private final UserRepository userRepository;
    private final SolveRepository solveRepository;
    private final SolveTimeCompare solveTimeCompare;

    @Value("${ScrambleApi.url}")
    private String constApiUrl;

    @Autowired
    public TimerService(UserRepository userRepository, SolveRepository solveRepository, SolveTimeCompare solveTimeCompare) {
        this.userRepository = userRepository;
        this.solveRepository = solveRepository;
        this.solveTimeCompare = solveTimeCompare;
    }

    public boolean isValidCube(String cube) {
        return switch (cube.toLowerCase()) {
            case "2x2", "3x3", "4x4", "5x5", "6x6", "7x7", "pyraminx" -> true;
            default -> false;
        };
    }

    public String getRandomScramble(String cube) throws IOException, CubeNotValidException {
        if (!isValidCube(cube))
            throw new CubeNotValidException("Cube is not valid");

        String url = constApiUrl + cube;

        Document document = Jsoup.connect(url).get();

        return document.text();
    }

    public static int toSeconds(String time) {
        // Incorrect input
        if (!time.contains(":"))
            return -1;

        String[] minutesAndSeconds = time.split(":");

        return (minutesAndSeconds.length == 3 ? Integer.parseInt(minutesAndSeconds[0]) * 60 * 100 +
                (Integer.parseInt(minutesAndSeconds[1]) * 100 + Integer.parseInt(minutesAndSeconds[2])) :
                (Integer.parseInt(minutesAndSeconds[0]) * 100 + Integer.parseInt(minutesAndSeconds[1])));
    }

    public static String convertToTime(int timeInMillis) {
        // Getting time
        int minutes = timeInMillis / (60 * 100);
        int seconds = (timeInMillis / 100) % 60;
        int millis = timeInMillis % 100;

        return (minutes < 10 ? minutes == 0 ? "" : "0" + minutes + ":" : minutes + ":") +
                (seconds < 10 ? "0" + seconds + ":" : seconds + ":") +
                (millis < 10 ? "0" + millis : millis);
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
        int secondsMax = toSeconds(lastFiveSolve.stream().max(solveTimeCompare).get().getTime());
        int secondsMin = toSeconds(lastFiveSolve.stream().min(solveTimeCompare).get().getTime());

        int timeWithoutMaxAndMin = totalTimeInMillis - secondsMin - secondsMax;

        // Get arithmetic mean
        timeWithoutMaxAndMin /= 3;

        return convertToTime(timeWithoutMaxAndMin);
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
        int secondsMax = toSeconds(lastFiveSolve.stream().max(solveTimeCompare).get().getTime());
        int secondsMin = toSeconds(lastFiveSolve.stream().min(solveTimeCompare).get().getTime());

        int timeWithoutMaxAndMin = totalTimeInMillis - secondsMin - secondsMax;

        // Get arithmetic mean
        timeWithoutMaxAndMin /= 10;

        return convertToTime(timeWithoutMaxAndMin);
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
        String validBody = body.substring(1, body.length() - 1);

        // Split the request output
        String[] requestParameters = validBody.split(",");

        if (RequestBodyValidation.isValidTime(requestParameters[1]) && RequestBodyValidation.isValidCube(requestParameters[2])) {
            // Create output solve entity if request body is valid
            SolveEntity solve = new SolveEntity(requestParameters[0], requestParameters[1], requestParameters[2], userRepository.findByUsername(username));

            // Save solve
            solveRepository.save(solve);
        }

    }

}
