package com.teamProject.lostArkProject.teaching.controller;


import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorListDTO;
//import com.teamProject.lostArkProject.teaching.service.NotificationService;
import com.teamProject.lostArkProject.teaching.service.TeachingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/teaching")
public class TeachingController {

    @Autowired
    private TeachingService teachingService;

    @GetMapping("/newMentor")
    public String newMentor(HttpSession session) {
        // 세션에서 "member" 객체 확인
        Object member = session.getAttribute("member");

        if (member == null) {
            // 세션에 "member" 객체가 없으면 접근 불가
            return "redirect:/member/signin"; // 로그인 페이지로 리다이렉트
        }
        // "member" 객체가 존재하면 페이지 반환
        return "teaching/newMentor";
    }



    @PostMapping("/newMentor")
    public String newMentor(@ModelAttribute MentorDTO mentorDTO,
                            @RequestParam("mentorContentId[]") String[] contentIds,
                            HttpSession session) {

        Member memberObj = (Member) session.getAttribute("member");
        if (memberObj == null) {
            return "redirect:/member/signin";
        }

        // ✅ Member 클래스에 정의된 memberId 사용
        String memberId = memberObj.getMemberId();
        mentorDTO.setMentorMemberId(memberId);

        String joinedContentIds = String.join(", ", contentIds);
        mentorDTO.setMentorContentId(joinedContentIds);

        teachingService.newMentor(mentorDTO);
        return "redirect:/teaching/mentorList";
    }


    @GetMapping("/mentorList")
    public String mentorList(HttpSession session, Model model) {
        Object memberObj = session.getAttribute("member");
        if (memberObj == null) {
            return "redirect:/member/signin";
        }

        // Member 클래스에 맞게 캐스팅
        Member member = (Member) memberObj;
        String loginMemberId = member.getMemberId(); // 현재 로그인한 사용자의 ID

        // 전체 멘토 리스트 조회
        List<MentorListDTO> allMentors = teachingService.getMentorList();

        // 🔥 로그인한 사용자 자신은 제외
        List<MentorListDTO> filteredMentors = allMentors.stream()
                .filter(mentor -> !loginMemberId.equals(mentor.getMentorMemberId()))
                .toList();

        model.addAttribute("mentors", filteredMentors);
        return "teaching/mentorList";
    }


//    @GetMapping("/teaching/mentorListDetail/{mentorMemberId}")
//    public String mentorListDetail(@PathVariable("mentorMemberId") String mentorMemberId, Model model) {
//        List<MentorListDTO> mentors = teachingService.getMentorDetail(mentorMemberId);
//        model.addAttribute("mentors", mentors);
//        //아이디로 상세정보가져오기
//        return "teaching/mentorListDetail";
//    }

    @PostMapping("/mentorListDetail")
    public String mentorListDetail(@RequestParam("mentorMemberId") String mentorMemberId, Model model) {
        List<MentorListDTO> mentors = teachingService.getMentorDetail(mentorMemberId);
        model.addAttribute("mentors", mentors);
        return "teaching/mentorListDetail";
    }



    // 멘티 신청 상태 + ACCEPTED 시 디스코드 ID 포함
    @GetMapping("/mentee/apply-status-detail/{menteeMemberId}")
    @ResponseBody
    public List<Map<String, Object>> getApplyStatusWithDiscord(@PathVariable String menteeMemberId) {
        List<Map<String, Object>> list = teachingService.getApplyStatusByMentee(menteeMemberId);
        for (Map<String, Object> entry : list) {
            if ("ACCEPTED".equals(entry.get("apply_status"))) {
                String mentorId = (String) entry.get("mentor_member_id");
                String discordId = teachingService.getMentorDiscordId(mentorId);
                entry.put("discord_id", discordId);
            }
        }
        return list;
    }



    @PostMapping("/applyMentee")
    public String applyMentee(@RequestParam("mentorMemberId") String mentorId,
                              @RequestParam("menteeMemberId") String menteeId,
                              RedirectAttributes redirectAttributes) {

        // 중복 신청 여부 확인
        if (teachingService.isDuplicateMenteeApply(mentorId, menteeId)) {
            redirectAttributes.addFlashAttribute("applyError", "이미 신청 이력이 있습니다.");
            return "redirect:/teaching/mentorList";
        }

        MenteeApplyDTO dto = new MenteeApplyDTO();
        dto.setMentor_member_id(mentorId);
        dto.setMentee_member_id(menteeId);
        dto.setApply_status("REQUESTED");

        teachingService.insertMenteeApply(dto);
        return "redirect:/teaching/mentorList";
    }

    @ModelAttribute("menteeMemberId")
    public String getLoggedInMenteeId(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        return member != null ? member.getMemberId() : null;
    }

    @GetMapping("/mentor/requested-applies")
    @ResponseBody
    public List<Map<String, Object>> getRequestedApplies(HttpSession session) {
        Member member = (Member) session.getAttribute("member");
        if (member == null) {
            return Collections.emptyList();
        }
        return teachingService.getRequestedAppliesByMentor(member.getMemberId());
    }
}
