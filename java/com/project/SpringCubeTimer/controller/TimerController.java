package com.project.SpringCubeTimer.controller;

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
    public String timerPage(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                            @CookieValue(name = "lastSolve", defaultValue = "00:00") String lastSolveTime,
                            @CookieValue(name = "saveSolve", defaultValue = "false") String disabledSaveMode,
                            @PathVariable("cube") String cube, Model model) throws IOException, CubeNotValidException {
        return timerService.timerPage(lastSolveTime, disabledSaveMode, cube, username, model);
    }

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE)
    public void getRequestFromJS(@CookieValue(name = "username", defaultValue = "UNKNOWN") String username,
                           @RequestBody String body) {
        timerService.saveSolve(username, body);
    }

    @GetMapping("/plus2")
    public String addPlus2ToSeconds(@RequestParam Long solveId,
                                    @RequestParam String page) {
        return timerService.addPlus2(solveId, page);
    }

    @GetMapping("/DNF")
    public String makeSolveDNF(@RequestParam Long solveId,
                               @RequestParam String page) {
        return timerService.makeDNF(solveId, page);
    }

}
