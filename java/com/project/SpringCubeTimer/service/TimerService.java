package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.entity.consts.ValidationConst;
import com.project.SpringCubeTimer.exception.CubeNotValidException;
import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.utils.TimerUtils;
import com.project.SpringCubeTimer.validate.RequestBodyValidation;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.io.IOException;
import java.net.ConnectException;

import static com.project.SpringCubeTimer.validate.RequestBodyValidation.isValidCube;

@Service
public class TimerService {

    private final UserRepository userRepository;
    private final SolveRepository solveRepository;
    private final TimerUtils timerUtils;

    @Value("${ScrambleApi.url}")
    private String constApiUrl;

    @Autowired
    public TimerService(UserRepository userRepository, SolveRepository solveRepository, TimerUtils timerUtils) {
        this.userRepository = userRepository;
        this.solveRepository = solveRepository;
        this.timerUtils = timerUtils;
    }

    public String getRandomScramble(String cube) throws IOException, CubeNotValidException {
        if (!isValidCube(cube))
            throw new CubeNotValidException("Cube is not valid");

        String url = constApiUrl + cube;

        try {
            Document document = Jsoup.connect(url).get();
            return document.text();
        } catch (ConnectException e) {
            return ValidationConst.FAILED_TO_GET_SCRAMBLE_MESSAGE;
        }
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

    public String timerPage(String lastSolveTime, String disabledSaveMode, String cube, String username, Model model) throws IOException, CubeNotValidException {
        if (username.equals("UNKNOWN"))
            return "redirect:/login";

        // Add attributes
        model.addAttribute("cube", cube);
        model.addAttribute("username", username);
        model.addAttribute("lastSolveTime", lastSolveTime);
        model.addAttribute("averageOf5", disabledSaveMode.equals("true") ? "--:--" : timerUtils.getAverageOf5(username, cube));
        model.addAttribute("averageOf12", disabledSaveMode.equals("true") ? "--:--" : timerUtils.getAverageOf12(username, cube));
        model.addAttribute("scramble", getRandomScramble(cube));

        return "timer.html";
    }

    public void saveSolve(String username, String body) {

        // Remove first and last character
        String validBody = body.substring(1, body.length() - 1);

        // Split the request output
        String[] requestParameters = validBody.split(",");

        if (RequestBodyValidation.isValidTime(requestParameters[1]) && isValidCube(requestParameters[2])) {
            // Create output solve entity if request body is valid
            SolveEntity solve = new SolveEntity(requestParameters[0], requestParameters[1], requestParameters[2],
                    ValidationConst.PENALTY_DEFAULT_VALUE, userRepository.findByUsername(username));

            // Save solve
            solveRepository.save(solve);
        }
    }

    public String addPlus2(Long solveId, String page) {
        if (solveRepository.findById(solveId).isEmpty())
            return "redirect:/profile?cube=3x3&page=0";

        SolveEntity solve = solveRepository.findById(solveId).get();

        if (solve.getPenalty().equals(ValidationConst.PENALTY_DEFAULT_VALUE)) {
            String newTime = convertToTime(toSeconds(solve.getTime()) + 200);

            solve.setPenalty(newTime);
            solveRepository.save(solve);
        } else {
            solve.setPenalty(ValidationConst.PENALTY_DEFAULT_VALUE);
            solveRepository.save(solve);
        }
        return "redirect:/profile?cube=3x3&page=" + page;
    }

    public String makeDNF(Long solveId, String page) {
        if (solveRepository.findById(solveId).isEmpty())
            return "redirect:/profile?cube=3x3&page=0";

        SolveEntity solve = solveRepository.findById(solveId).get();

        if (solve.getPenalty().equals(ValidationConst.PENALTY_DEFAULT_VALUE)) {
            solve.setPenalty("DNF");
            solveRepository.save(solve);
        } else {
            solve.setPenalty(ValidationConst.PENALTY_DEFAULT_VALUE);
            solveRepository.save(solve);
        }
        return "redirect:/profile?cube=3x3&page=" + page;
    }

    public String deleteSolve(Long id, Integer page, String cube) {
        if (!solveRepository.existsById(id))
            return "redirect:/profile";

        solveRepository.deleteById(id);

        return String.format("redirect:/profile?cube=%s&page=%s", cube, page);
    }
}
