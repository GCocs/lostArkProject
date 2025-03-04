package com.teamProject.lostArkProject.academy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AcademyController {
    @GetMapping("/academy")
    public String academy() {
        return "academy/academyList";
    }
}
