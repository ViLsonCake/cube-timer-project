package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.comparator.SolveTimeCompare;
import com.project.SpringCubeTimer.comparator.SortByAverageTimeComparator;
import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.entity.UserEntity;
import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {

    private final SolveRepository solveRepository;
    private final UserRepository userRepository;
    private final SolveTimeCompare solveTimeCompare;
    private final SortByAverageTimeComparator averageTimeComparator;

    @Autowired
    public RatingService(SolveRepository solveRepository, UserRepository userRepository, SolveTimeCompare solveTimeCompare, SortByAverageTimeComparator averageTimeComparator) {
        this.solveRepository = solveRepository;
        this.userRepository = userRepository;
        this.solveTimeCompare = solveTimeCompare;
        this.averageTimeComparator = averageTimeComparator;
    }

    public String getTopFifteenOrLessUsers(String cookieUsername, String cube, Model model) {
        Iterable<UserEntity> users = userRepository.findAll();

        // Output list
        List<String[]> topUsers = new ArrayList<>();

        for (UserEntity user : users) {
            // Find all user solves
            List<SolveEntity> userSolves = solveRepository.findAllSolveByIdAndCube(user.getUserId(), cube);

            // If user don't have solves
            if (userSolves.isEmpty())
                continue;

            // User values
            String username = user.getUsername();
            String personalBest = getPersonalBest(userSolves);
            String averageTime = getAverageTime(userSolves);

            topUsers.add(new String[]{username, personalBest, averageTime}); // Add array with user values
        }

        // Sort list by solve time
        topUsers.sort(averageTimeComparator);

        if (topUsers.size() > 15)
            topUsers.subList(0, 15);

        model.addAttribute("users", topUsers);
        model.addAttribute("username", cookieUsername);

        return "rating.html";
    }

    public String getPersonalBest(List<SolveEntity> userSolves) {
        return userSolves.stream().min(solveTimeCompare).get().getTime();
    }

    public String getAverageTime(List<SolveEntity> userSolves) {
        int totalTimeInMillis = 0;

        for (SolveEntity userSolve : userSolves) {
            totalTimeInMillis += TimerService.toSeconds(userSolve.getTime());
        }

        return TimerService.convertToTime(totalTimeInMillis / userSolves.size());
    }

}
