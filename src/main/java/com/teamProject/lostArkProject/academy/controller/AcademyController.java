package com.teamProject.lostArkProject.academy.controller;

import com.teamProject.lostArkProject.academy.domain.AcademyBoard;
import com.teamProject.lostArkProject.academy.dto.AcademyRequestDTO;
import com.teamProject.lostArkProject.academy.service.AcademyService;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.common.exception.UnauthorizedException;
import com.teamProject.lostArkProject.member.config.SessionUtils;
import com.teamProject.lostArkProject.member.domain.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Validated
@Slf4j
public class AcademyController {
    private final AcademyService academyService;

    @GetMapping("/academy")
    public String getAcademyList(@RequestParam(value = "page", defaultValue = "1") int page, Model model) {
        PaginatedResponseDTO<AcademyBoard> response = academyService.getAcademyList(page);
        model.addAttribute("response", response);
        return "academy/academyList";
    }

    @GetMapping("/academy/{academyId}")
    public String getAcademyDetail(Model model,
                                   @PathVariable int academyId) {
        AcademyBoard academy = academyService.getAcademy(academyId);
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

    @GetMapping("/academy/{academyId}/edit")
    public String getEditAcademy(Model model,
                                 HttpSession session,
                                 @PathVariable int academyId) {
        Member member = SessionUtils.getMember(session);

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        AcademyBoard academy = academyService.getAcademyWithPermissionCheck(academyId, member);

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

    @PostMapping("/academy/{academyId}/edit")
    public String editAcademyPost(HttpSession session,
                                   @PathVariable int academyId,
                                   @ModelAttribute AcademyRequestDTO academyRequestDTO) {
        Member member = SessionUtils.getMember(session);

        if (member == null) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        academyService.editAcademyPost(academyId, academyRequestDTO, member);
        return "redirect:/academy";
    }
}
