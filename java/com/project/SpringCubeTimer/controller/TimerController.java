package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.exception.CubeNotValidException;
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

    private final TimerService timerService;

    @Autowired
    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @GetMapping("/{cube}")
    public String timerPage(@CookieValue(name = "isLogged", defaultValue = "false") boolean isLogged, @PathVariable("cube") String cube, Model model) throws IOException, CubeNotValidException {
        if (!isLogged)
            return "redirect:/login";

        timerService.timerPage(model, cube);

        return "timer";
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void getRequestFromJS(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                           @RequestBody String body) {
        System.out.println(body);
        System.out.println(username);

    }
}
