package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.service.ProfileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping
    public String profilePage(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                              @RequestParam(required = false, defaultValue = "3x3") String cube,
                              @RequestParam(required = false, defaultValue = "0") int page,
                              Model model) {
        return profileService.getProfilePage(username, cube, page, model);
    }

    @PostMapping
    public String changeUsername(@CookieValue(name = "username", defaultValue = "UNKNOWN") String oldUsername,
                                 @RequestParam String newUsername,
                                 HttpServletResponse response) {
        return profileService.changeUsername(response, oldUsername, newUsername);
    }

    @GetMapping("/change-password")
    public String changePasswordPage() {
        return profileService.getChangePasswordPage();
    }

    @PostMapping("/change-password")
    public String changePassword(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        return profileService.changePassword(username, password, confirmPassword, model);
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordPage(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                                     Model model) {
        return profileService.getForgotPasswordPage(username, model);
    }

    @PostMapping("/forgot-password")
    public String userEmail(@RequestParam String email,
                               HttpServletResponse response,
                               Model model) {
        return profileService.getLoginEmail(response, email, model);
    }

    @GetMapping("/enter-code")
    public String enterCodePage(Model model) {
        return profileService.getEnterCodePage(model);
    }

    @PostMapping("/enter-code")
    public String getUserCode(@CookieValue(name = "email") String email,
                              @CookieValue(name = "securityCode") String securityCode,
                              @RequestParam String code,
                              HttpServletResponse response,
                              Model model) {
        return profileService.getUserCode(response, email, securityCode, code, model);
    }
}
