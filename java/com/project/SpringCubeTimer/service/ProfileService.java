package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.entity.UserEntity;
import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.validate.RequestBodyValidation;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final SolveRepository solveRepository;
    private final RatingService ratingService;
    private final int size;

    @Autowired
    public ProfileService(UserRepository userRepository, SolveRepository solveRepository, RatingService ratingService) {
        this.userRepository = userRepository;
        this.solveRepository = solveRepository;
        this.ratingService = ratingService;
        this.size = 20;
    }

    public String getProfilePage(String username, String cube, int page, Model model) {
        if (username.equals("UNKNOWN"))
            return "redirect:/login";

        // If page less than zero
        if (page < 0)
            return String.format("redirect:/profile?cube=%s&page=%s", cube, 0);

        // Get current user page with solve
        Page<SolveEntity> solves = solveRepository.findAllByCube(cube, PageRequest.of(
                page, size, Sort.by("solveId").descending()));

        // If page don't exist, redirect to last exist page
        if (page >= solves.getTotalPages() && page != 0)
            return String.format("redirect:/profile?cube=%s&page=%s", cube, solves.getTotalPages() - 1);

        if (page == 0 && solves.getTotalPages() == 0) {
            model.addAttribute("username", username);
            model.addAttribute("solveNotExist", true);
            return "profile";
        }

        // Get current user solves by cube
        List<SolveEntity> userSolves = solveRepository.findAllSolveByIdAndCube(userRepository.findByUsername(username).getUserId(), cube);

        model.addAttribute("username", username);
        model.addAttribute("cube", cube);
        model.addAttribute("page", page);
        model.addAttribute("personalBest",
                ratingService.getPersonalBest(userSolves));
        model.addAttribute("average", ratingService.getAverageTime(userSolves));
        model.addAttribute("solves", solves);
        model.addAttribute("totalPages", solves.getTotalPages());
        model.addAttribute("solveNotExist", false);

        return "profile";
    }

    public String changeUsername(HttpServletResponse response, String oldUsername, String newUsername) {
        if (userRepository.findByUsername(newUsername) != null)
            return "redirect:/profile?cube3x3&page=0";

        // Find user, change username, save
        UserEntity changedUser = userRepository.findByUsername(oldUsername);
        changedUser.setUsername(newUsername);
        userRepository.save(changedUser);

        // Change cookie value
        LoginService.makeLogged(response, newUsername);

        return "redirect:/profile?cube?3x3&page=0";
    }

}
