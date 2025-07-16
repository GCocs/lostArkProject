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
                              @RequestParam("rejectReason") String rejectReason,
                              @RequestParam("blockMentee") String blockMentee,
                              HttpSession session) {
        
        System.out.println("=== 거절 요청 받음 ===");
        System.out.println("요청된 멘토 ID: " + mentorMemberId);
        System.out.println("요청된 멘티 ID: " + menteeMemberId);
        System.out.println("거절 사유: " + rejectReason);
        System.out.println("차단 여부: " + blockMentee);
        
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            System.out.println("세션에 사용자 정보가 없음, 로그인 페이지로 리다이렉트");
            return "redirect:/member/signin";
        }

        System.out.println("현재 로그인된 사용자: " + member.getMemberId());
        
        boolean shouldBlock = "Y".equals(blockMentee);
        System.out.println("차단 처리 여부: " + shouldBlock);
        
        try {
            messageService.rejectMenteeApplyWithReason(mentorMemberId, menteeMemberId, rejectReason, shouldBlock);
            System.out.println("거절 처리 성공, 메시지 목록으로 리다이렉트");
        } catch (Exception e) {
            System.out.println("거절 처리 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        
        return "redirect:/message/list";
    }

}
