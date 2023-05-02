package com.project.SpringCubeTimer.service;

import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.entity.UserEntity;
import com.project.SpringCubeTimer.repository.SolveRepository;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.sendMail.MailSender;
import com.project.SpringCubeTimer.service.serviceConst.ServiceConst;
import com.project.SpringCubeTimer.validate.RequestBodyValidation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Random;

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
        model.addAttribute("solveCount", userSolves.size());
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

    public String getChangePasswordPage() {
        return "changepass";
    }

    public String changePassword(String username, String password, String confirmPassword, Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("passwordsDoNotMatch", "Passwords don't match");
            return "changepass";
        }

        // If user not exist
        if (userRepository.findByUsername(username) == null)
            return "changepass";

        // Find user change password and save
        UserEntity user = userRepository.findByUsername(username);
        user.setPassword(password);

        userRepository.save(user);

        return "redirect:/timer/3x3";

    }

    public String getForgotPasswordPage(String username, Model model) {

        // Find user email
        String email = userRepository.findByUsername(username).getEmail();

        model.addAttribute("email", email);

        return "forgotpass";
    }

    public String getLoginEmail(HttpServletResponse response, String email, Model model) {
        // If email not exist
        if (userRepository.findByEmail(email) == null) {
            model.addAttribute("emailNotExist", "Email is not exist");
            return "forgotpass";
        }

        UserEntity user = userRepository.findByEmail(email);

        // Generate code
        Random random = new Random();
        String securityCode = String.format("%s-%s-%s", random.nextInt(10, 100),
                                                        random.nextInt(10, 100),
                                                        random.nextInt(10, 100));

        // Send code to user
        MailSender mailSender = new MailSender(user.getEmail(), ServiceConst.MESSAGE_SUBJECT,
                String.format(ServiceConst.MESSAGE_TEXT, securityCode));
//        mailSender.send();

        System.out.println(securityCode);

        addEmailToCookies(response, email);
        addCodeToCookies(response, securityCode);

        return "redirect:/profile/enter-code";
    }

    public void addEmailToCookies(HttpServletResponse response, String email) {
        if (userRepository.findByEmail(email) == null) {
            return;
        }

        // Create new cookie
        Cookie emailCookie = new Cookie("email", email);
        emailCookie.setMaxAge(120);

        response.addCookie(emailCookie);
    }

    public void removeEmailFromCookies(HttpServletResponse response) {
        // Create new cookie
        Cookie emailCookie = new Cookie("email", "");
        emailCookie.setMaxAge(0);

        response.addCookie(emailCookie);
    }

    public void addCodeToCookies(HttpServletResponse response, String code) {
        // Create new cookie
        Cookie codeCookie = new Cookie("securityCode", code);
        codeCookie.setMaxAge(120);

        response.addCookie(codeCookie);

    }

    public void removeCodeFromCookies(HttpServletResponse response) {
        // Create new cookie
        Cookie codeCookie = new Cookie("securityCode", "");
        codeCookie.setMaxAge(0);

        response.addCookie(codeCookie);
    }

    public String getEnterCodePage(Model model) {
        return "entercode";
    }

    public String getUserCode(HttpServletResponse response, String email,
                              String securityCode, String userCode, Model model) {
        if (!userCode.equals(securityCode)) {
            model.addAttribute("wrongCodeMessage", "Code is wrong");
            return "entercode";
        }

        LoginService.makeLogged(response, userRepository.findByEmail(email).getUsername());

        // Remove support cookies
        removeEmailFromCookies(response);
        removeCodeFromCookies(response);

        return "redirect:/profile/change-password";
    }
}
