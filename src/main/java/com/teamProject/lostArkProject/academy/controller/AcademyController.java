package com.teamProject.lostArkProject.academy.controller;

import com.teamProject.lostArkProject.academy.domain.AcademyBoard;
import com.teamProject.lostArkProject.academy.dto.AcademyRequestDTO;
import com.teamProject.lostArkProject.academy.service.AcademyService;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.member.config.SessionUtils;
import com.teamProject.lostArkProject.member.domain.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AcademyController {
    private final AcademyService academyService;

    @GetMapping("/academy")
    public String getAcademyList(Model model) {
        PaginatedResponseDTO<AcademyBoard> response = academyService.getAcademyList(1);
        model.addAttribute("response", response);
        return "academy/academyList";
    }

    @GetMapping("/academy/{id}")
    public String getAcademyDetail(Model model,
                                   @PathVariable int id) {
        AcademyBoard academy = academyService.getAcademy(id);
        model.addAttribute("academy", academy);
        return "academy/academyDetail";
    }

    @GetMapping("/academy/write")
    public String getWriteAcademy(HttpSession session) {
        Member member = SessionUtils.getMember(session);

        if (member == null) {
            return "redirect:/member/signin";
        }

        return "academy/academyWrite";
    }

    @GetMapping("/academy/{id}/edit")
    public String editAcademyDetail(Model model,
                                    @PathVariable int id) {
        AcademyBoard academy = academyService.getAcademy(id);
        model.addAttribute("academy", academy);
        return "academy/academyEdit";
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
