package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping("/rating")
    public String ratingPage(@RequestParam("cube") String cube, Model model) {

        return ratingService.getTopFifteenOrLessUsers(cube, model);
    }
}
