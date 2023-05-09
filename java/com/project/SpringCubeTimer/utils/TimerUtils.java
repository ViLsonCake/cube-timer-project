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

        lastFiveSolve.forEach(System.out::println);

        int DNFcount = 0;
        int nonDNFcount = -2;
        int totalTimeInMillis = 0;

        // If user have less than 5 solves
        if (lastFiveSolve.size() < 5) {
            return "--:--";
        }

        for (SolveEntity solve : lastFiveSolve) {
            if (solve.getTime().equalsIgnoreCase("DNF")) {
                DNFcount++;
            } else {
                nonDNFcount++;
                totalTimeInMillis += TimerService.toSeconds(solve.getTime());
            }
        }

        // If ao5 equals DNF
        if (DNFcount >= 2)
            return "DNF";

        // Subtract the best and words result
        int secondsMax = TimerService.toSeconds(lastFiveSolve.stream().max(solveTimeCompare).get().getTime());
        int secondsMin = TimerService.toSeconds(lastFiveSolve.stream().min(solveTimeCompare).get().getTime());

        int timeWithoutMaxAndMin = totalTimeInMillis - secondsMin - secondsMax;

        // Get arithmetic mean
        timeWithoutMaxAndMin /= nonDNFcount;

        return TimerService.convertToTime(timeWithoutMaxAndMin);
    }

    public String getAverageOf12(String username, String cube) {
        List<SolveEntity> lastFiveSolve = solveRepository.findLastTwelveSolveByUserIdAndCube(userRepository.findByUsername(username).getUserId(), cube);

        // If user have less than 12 solves
        if (lastFiveSolve.size() < 12) {
            return "--:--";
        }

        int totalTimeInMillis = 0;

        for (SolveEntity solve : lastFiveSolve) {
            totalTimeInMillis += TimerService.toSeconds(solve.getTime());
        }
        // Subtract the best and words result
        int secondsMax = TimerService.toSeconds(lastFiveSolve.stream().max(solveTimeCompare).get().getTime());
        int secondsMin = TimerService.toSeconds(lastFiveSolve.stream().min(solveTimeCompare).get().getTime());

        int timeWithoutMaxAndMin = totalTimeInMillis - secondsMin - secondsMax;

        // Get arithmetic mean
        timeWithoutMaxAndMin /= 10;

        return TimerService.convertToTime(timeWithoutMaxAndMin);
    }
}
