package com.project.SpringCubeTimer.utils;

import com.project.SpringCubeTimer.comparator.SolveTimeCompare;
import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimerUtils {

    private final UserRepository userRepository;
    private final SolveRepository solveRepository;
    private final SolveTimeCompare solveTimeCompare;

    @Autowired
    public TimerUtils(UserRepository userRepository, SolveRepository solveRepository, SolveTimeCompare solveTimeCompare) {
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

    public String getAverageOf5(String username, String cube) {
        List<SolveEntity> lastFiveSolve = solveRepository.findLastFiveSolveByUserIdAndCube(userRepository.findByUsername(username).getUserId(), cube);

        int DNFcount = 0;
        int totalTimeInMillis = 0;

        // If user have less than 5 solves
        if (lastFiveSolve.size() < 5) {
            return "--:--";
        }
        SolveEntity minSolve = lastFiveSolve.stream().min(solveTimeCompare).get();

        int secondsMax = TimerService.toSeconds(lastFiveSolve.stream().max(solveTimeCompare).get().getTime());
        int secondsMin = TimerService.toSeconds(!(minSolve.getPenalty().equals("None")) && !(minSolve.getPenalty().equals("DNF"))
                ? minSolve.getPenalty() : minSolve.getTime());

        for (SolveEntity solve : lastFiveSolve) {
            if (solve.getPenalty().equals("DNF")) {
                DNFcount++;

                // If ao5 equals DNF
                if (DNFcount >= 2)
                    return "DNF";

                secondsMax = 0;
            } else if (solve.getPenalty().equals("None")){
                if (TimerService.toSeconds(solve.getTime()) < secondsMin)
                    secondsMin = TimerService.toSeconds(solve.getTime());

                totalTimeInMillis += TimerService.toSeconds(solve.getTime());
            } else {
                if (TimerService.toSeconds(solve.getPenalty()) < secondsMin)
                    secondsMin = TimerService.toSeconds(solve.getPenalty());
                totalTimeInMillis += TimerService.toSeconds(solve.getPenalty());
            }
        }
        // Subtract the best and words result
        int timeWithoutMaxAndMin = totalTimeInMillis - secondsMin - secondsMax;

        // Get arithmetic mean
        timeWithoutMaxAndMin /= 3;

        return TimerService.convertToTime(timeWithoutMaxAndMin);
    }

    public String getAverageOf12(String username, String cube) {
        List<SolveEntity> lastTwelveSolves = solveRepository.findLastTwelveSolveByUserIdAndCube(userRepository.findByUsername(username).getUserId(), cube);

        int DNFcount = 0;
        int totalTimeInMillis = 0;

        // If user have less than 12 solves
        if (lastTwelveSolves.size() < 12) {
            return "--:--";
        }
        SolveEntity minSolve = lastTwelveSolves.stream().min(solveTimeCompare).get();

        int secondsMax = TimerService.toSeconds(lastTwelveSolves.stream().max(solveTimeCompare).get().getTime());
        int secondsMin = TimerService.toSeconds(!(minSolve.getPenalty().equals("None")) && !(minSolve.getPenalty().equals("DNF"))
                ? minSolve.getPenalty() : minSolve.getTime());

        for (SolveEntity solve : lastTwelveSolves) {
            if (solve.getPenalty().equals("DNF")) {
                DNFcount++;

                // If ao5 equals DNF
                if (DNFcount >= 2)
                    return "DNF";

                secondsMax = TimerService.toSeconds(solve.getTime());
            } else if (solve.getPenalty().equals("None")) {
                if (TimerService.toSeconds(solve.getTime()) < secondsMin)
                    secondsMin = TimerService.toSeconds(solve.getTime());

                totalTimeInMillis += TimerService.toSeconds(solve.getTime());
            } else {
                if (TimerService.toSeconds(solve.getTime()) < secondsMin)
                    secondsMin = TimerService.toSeconds(solve.getTime());

                totalTimeInMillis += TimerService.toSeconds(solve.getPenalty());
            }
        }
        // Subtract the best and words result
        int timeWithoutMaxAndMin = totalTimeInMillis - secondsMin - secondsMax;

        // Get arithmetic mean
        timeWithoutMaxAndMin /= 10;

        return TimerService.convertToTime(timeWithoutMaxAndMin);
    }
}
