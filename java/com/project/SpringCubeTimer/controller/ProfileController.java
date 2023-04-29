package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/profile")
    public String profilePage(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                              @RequestParam(required = false, defaultValue = "3x3") String cube,
                              @RequestParam(required = false, defaultValue = "0") int page,
                              Model model) {
        return profileService.getProfilePage(username, cube, page, model);
    }

}
