package com.teamProject.lostArkProject.academy.controller;

import com.teamProject.lostArkProject.academy.dto.AcademyRequestDTO;
import com.teamProject.lostArkProject.academy.service.AcademyService;
import com.teamProject.lostArkProject.common.exception.UnauthorizedException;
import com.teamProject.lostArkProject.member.config.SessionUtils;
import com.teamProject.lostArkProject.member.domain.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AcademyController {
    private final AcademyService academyService;

    @GetMapping("/academy")
    public String getAcademyList() {
        return "academy/academyList";
    }

    @GetMapping("/academy/write")
    public String getWriteAcademy(HttpSession session) {
        Member member = SessionUtils.getMember(session);

        if (member == null) {
            return "redirect:/member/signin";
        }

        return "academy/academyWrite";
    }

    @PostMapping("/academy/write")
    public String writeAcademyPost(HttpSession session,
                                   @ModelAttribute AcademyRequestDTO academyRequestDTO) {
        Member member = SessionUtils.getMember(session);

        if (member == null) {
            return "redirect:/member/signin";
        }

        academyService.createAcademyPost(academyRequestDTO, member);
        return "redirect:/academy";
    }
}
