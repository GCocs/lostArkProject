package com.teamProject.lostArkProject.teaching.controller;

import com.teamProject.lostArkProject.member.domain.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
        // 세션에서 멘토 아이디 꺼내기 (세션에 저장된 객체 타입에 따라 캐스팅 필요)

        Member memberObj = (Member) session.getAttribute("member");
        if (memberObj == null) {
            return "redirect:/member/signin";
        }

        // Member 클래스에 정의된 memberId 사용
        String mentorMemberId = memberObj.getMemberId();

        // 파라미터 맵 생성
        Map<String, Object> param = new HashMap<>();
        param.put("menteeMemberId", menteeMemberId);
        param.put("mentorMemberId", mentorMemberId);

        // 서비스 호출
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

        // TeachingService의 getRequestedAppliesByMentor 사용
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
        return "mentorResultList";
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
    public String rejectReason(HttpSession session, Model model) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/signin";
        }
        return "/message/rejectReason";
    }

}
