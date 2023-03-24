package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.entity.SolveEntity;
import com.project.SpringCubeTimer.exception.CubeNotValidException;
import com.project.SpringCubeTimer.repository.UserRepository;
import com.project.SpringCubeTimer.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/timer")
public class TimerController {

    private final UserRepository userRepository;
    private final TimerService timerService;

    @Autowired
    public TimerController(UserRepository userRepository, TimerService timerService) {
        this.userRepository = userRepository;
        this.timerService = timerService;
    }

    @GetMapping("/{cube}")
    public String timerPage(@CookieValue(name = "username") String username, @CookieValue(name = "isLogged", defaultValue = "false") boolean isLogged, @PathVariable("cube") String cube, Model model) throws IOException, CubeNotValidException {
        if (!isLogged)
            return "redirect:/login";

        timerService.timerPage(model, cube, username);

        return "timer";
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void getRequestFromJS(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                           @RequestBody String body) {
//        System.out.println(body);
//        System.out.println(username);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(body);
        stringBuilder.deleteCharAt(0);
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        String[] data = stringBuilder.toString().split(",");
        SolveEntity solve = new SolveEntity();

        solve.setScramble(data[0]);
        solve.setTime(data[1]);
        solve.setCube(data[2]);

        System.out.println(solve.toString());


    }
}
