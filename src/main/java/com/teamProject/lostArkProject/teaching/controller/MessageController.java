package com.teamProject.lostArkProject.teaching.controller;

import com.teamProject.lostArkProject.member.domain.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.service.MessageService;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.teamProject.lostArkProject.teaching.service.TeachingService;


@Controller
@RequestMapping("/message")
public class MessageController {
    
    @Autowired
    private MessageService messageService;

    @Autowired
    private TeachingService teachingService;

    @GetMapping("/newMessageDetail")
    public String newMessageDetail(@RequestParam("menteeMemberId") String menteeMemberId, HttpSession session, Model model) {

        Member memberObj = (Member) session.getAttribute("member");
        if (memberObj == null) {
            return "redirect:/member/signin";
        }

        String mentorMemberId = memberObj.getMemberId();

        Map<String, Object> param = new HashMap<>();
        param.put("menteeMemberId", menteeMemberId);
        param.put("mentorMemberId", mentorMemberId);

        MenteeApplyDTO apply = messageService.getMenteeApplyDetail(param);
        Map<String, Object> menteeCharacter = messageService.getMenteeCharacterInfo(menteeMemberId);

        model.addAttribute("apply", apply);
        model.addAttribute("menteeMemberId", menteeMemberId);
        model.addAttribute("menteeCharacter", menteeCharacter);
        return "message/newMessageDetail";
    }


    @GetMapping("/list")
    public String getMessagelist(HttpSession session, Model model) {
        Member memberObj = (Member) session.getAttribute("member");
        if (memberObj == null) {
            return "redirect:/member/signin";
        }
        String mentorMemberId = memberObj.getMemberId();

        List<Map<String, Object>> requestedList = teachingService.getRequestedAppliesByMentor(mentorMemberId);

        model.addAttribute("requestedList", requestedList);
        return "message/messageList";
    }

    @GetMapping("/myRequest")
    public String myRequest(HttpSession session, Model model) {
        Member memberObj = (Member) session.getAttribute("member");
        if (memberObj == null) {
            return "redirect:/member/signin";
        }
        String mentorMemberId = memberObj.getMemberId();
        return "message/mentorResultList";
    }

    @GetMapping("/all-applies")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<Map<String, Object>> getAllMenteeApplies(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return java.util.Collections.emptyList();
        }
        return messageService.getAllMenteeAppliesByMentor(member.getMemberId());
    }

    @GetMapping("/my-applies")
    @org.springframework.web.bind.annotation.ResponseBody
    public List<Map<String, Object>> getAllMenteeAppliesByMentee(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return java.util.Collections.emptyList();
        }
        return messageService.getAllMenteeAppliesByMentee(member.getMemberId());
    }

    @GetMapping("/rejectReason")
    public String rejectReason(@RequestParam("menteeMemberId") String menteeMemberId, HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/signin";
        }
        model.addAttribute("menteeMemberId", menteeMemberId);
        return "/message/rejectReason";
    }

    @PostMapping("/rejectMentee")
    public String rejectMentee(@RequestParam("mentorMemberId") String mentorMemberId,
                              @RequestParam("menteeMemberId") String menteeMemberId,
                              @RequestParam("finalRejectReason") String rejectReason,
                              @RequestParam("blockMentee") String blockMentee,
                              HttpSession session) {
        
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/signin";
        }
        
        boolean shouldBlock = "Y".equals(blockMentee);
        messageService.rejectMenteeApplyWithReason(mentorMemberId, menteeMemberId, rejectReason, shouldBlock);
        
        return "redirect:/message/list";
    }

}
