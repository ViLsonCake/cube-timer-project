package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.entity.UserEntity;
import com.project.SpringCubeTimer.entity.consts.ValidationConst;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.utils.PasswordEncoder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class RegistrationService {

    private final UserRepository userRepository;
    @Autowired
    public RegistrationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(UserEntity user, String confirmPassword, HttpServletResponse response, Model model) {

        // Check invalid values
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("passwordNotMatch", ValidationConst.PASSWORDS_NOT_MATCH_MESSAGE);
            return "registration.html";
        } else if (userRepository.findByUsername(user.getUsername()) != null ||
                userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("UserExistError", ValidationConst.USER_ALREADY_EXIST_MESSAGE);
            return "registration.html";
        } else if (user.getUsername().equals("UNKNOWN")) {
            model.addAttribute("invalidName", ValidationConst.USERNAME_NOT_VALID_MESSAGE);
            return "registration.html";
        } else if (!isValidPassword(user.getPassword())) {
            model.addAttribute("invalidPassword", ValidationConst.PASSWORD_NOT_VALID_MESSAGE);
            return "registration.html";
        }

        // Encode password
        user.setPassword(PasswordEncoder.encode(user.getPassword()));

        // Save user to db and cookie session
        userRepository.save(user);
        LoginService.makeLogged(response, user.getUsername());

        return "redirect:/timer/3x3";

    }

    public boolean isValidPassword(String password) {
        Pattern passwordPattern = Pattern.compile(ValidationConst.REGEX_PASSWORD_PATTERN);
        Matcher passwordMatcher = passwordPattern.matcher(password);

        return passwordMatcher.find();
    }
}
