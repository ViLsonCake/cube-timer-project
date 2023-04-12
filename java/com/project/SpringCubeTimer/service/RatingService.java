package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    private final SolveRepository solveRepository;
    private final UserRepository userRepository;

    @Autowired
    public RatingService(SolveRepository solveRepository, UserRepository userRepository) {
        this.solveRepository = solveRepository;
        this.userRepository = userRepository;
    }

    public List<String[]> getTopFifteenOrLessUsers() {
        List<String[]> topUsers = solveRepository.findTopUsers();

        for (String[] value : topUsers) {
            value[0] = userRepository.findById(Long.parseLong(value[0])).get().getUsername();   // Replacing the user id with a username

            value[1] = String.format("%.2f", Double.parseDouble(value[1])); // Round to the second decimal place
        }

        return topUsers;
    }

}
