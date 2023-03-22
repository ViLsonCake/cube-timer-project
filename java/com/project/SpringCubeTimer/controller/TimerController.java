package com.project.SpringCubeTimer.controller;

import com.project.SpringCubeTimer.service.TimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

@Controller
@RequestMapping("/timer")
public class TimerController {

    private final TimerService timerService;

    @Autowired
    public TimerController(TimerService timerService) {
        this.timerService = timerService;
    }

    @GetMapping
    public String timerPage(Model model) throws IOException {
        timerService.timerPage(model);

        return "timer";
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void getRequest(@RequestBody String body) {
        System.out.println(body);

    }
}
