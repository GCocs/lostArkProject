package com.teamProject.lostArkProject.teaching.controller;


import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorListDTO;
//import com.teamProject.lostArkProject.teaching.service.NotificationService;
import com.teamProject.lostArkProject.teaching.service.MessageService;
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
import java.util.Set;
@Controller
@RequestMapping("/teaching")
public class TeachingController {

    @Autowired
    private TeachingService teachingService;

    @Autowired
    private MessageService messageService;

    @GetMapping("/newMentor")
    public String newMentor(HttpSession session) {
        // 세션에서 "member" 객체 확인
        Object member = session.getAttribute("member");

        if (member == null) {
            // 세션에 "member" 객체가 없으면 접근 불가
            return "redirect:/member/signin"; // 로그인 페이지로 리다이렉트
        }
        // 이미 멘토 이력이 있으면 수정 폼으로 리다이렉트
        String memberId = ((Member) member).getMemberId();
        if (teachingService.isMentorExists(memberId)) {
            return "redirect:/teaching/mentorUpdate";
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

        // Member 클래스에 정의된 memberId 사용
        String memberId = memberObj.getMemberId();
        mentorDTO.setMentorMemberId(memberId);

        String joinedContentIds = String.join(", ", contentIds);
        mentorDTO.setMentorContentId(joinedContentIds);

        teachingService.newMentor(mentorDTO);
        return "redirect:/teaching/mentorList";
    }

    

    @GetMapping("/mentorUpdate")
    public String mentorUpdate(HttpSession session, Model model) {
        Object member = session.getAttribute("member");
        if (member == null) {
            return "redirect:/member/signin";
        }
        String memberId = ((Member) member).getMemberId();
        Map<String, Object> mentorInfo = teachingService.getMentorInfoById(memberId);
        List<String> mentorContentIds = teachingService.getMentorContentIdsById(memberId);
        model.addAttribute("mentorInfo", mentorInfo);
        model.addAttribute("mentorContentIds", mentorContentIds);
        return "teaching/mentorUpdate";
    }   


    @PostMapping("/mentorUpdate")
    public String mentorUpdate(@ModelAttribute MentorDTO mentorDTO,
                            @RequestParam(value = "mentorContentId[]", required = false) String[] contentIds,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        Member memberObj = (Member) session.getAttribute("member");
        if (memberObj == null) {
            return "redirect:/member/signin";
        }

        String memberId = memberObj.getMemberId();
        mentorDTO.setMentorMemberId(memberId);

        // 콘텐츠 ID 처리 (아무것도 선택하지 않은 경우 대비)
        if (contentIds != null && contentIds.length > 0) {
            String joinedContentIds = String.join(", ", contentIds);
            mentorDTO.setMentorContentId(joinedContentIds);
        } else {
            // 아무것도 선택하지 않은 경우 빈 문자열로 설정
            mentorDTO.setMentorContentId("");
        }

        try {
            teachingService.updateMentor(mentorDTO);
            redirectAttributes.addFlashAttribute("successMessage", "멘토 정보가 성공적으로 수정되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "멘토 정보 수정 중 오류가 발생했습니다.");
            return "redirect:/teaching/mentorUpdate";
        }

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

        // 로그인한 사용자 자신은 제외
        List<MentorListDTO> filteredMentors = allMentors.stream()
                .filter(mentor -> !loginMemberId.equals(mentor.getMentorMemberId()))
                .toList();

        // 로그인한 멘티 ID
        String menteeId = ((Member) session.getAttribute("member")).getMemberId();
        // 이미 신청한 멘토 ID 목록 조회 (service/dao에서 구현 필요)
        Set<String> appliedMentorIds = teachingService.getAppliedMentorIdsByMentee(menteeId);
        model.addAttribute("appliedMentorIds", appliedMentorIds);

        model.addAttribute("mentors", filteredMentors);
        return "teaching/mentorList";
    }


    @PostMapping("/mentorListDetail")
    public String mentorListDetail(@RequestParam("mentorMemberId") String mentorMemberId, Model model) {
        List<MentorListDTO> mentors = teachingService.getMentorDetail(mentorMemberId);
        model.addAttribute("mentors", mentors);
        return "teaching/mentorListDetail";
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
        dto.setMentorMemberId(mentorId);
        dto.setMenteeMemberId(menteeId);
        dto.setApplyStatus("REQUESTED");

        teachingService.insertMenteeApply(dto);
        return "redirect:/teaching/mentorList";
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

    @PostMapping("/acceptMentee")
    public String acceptMentee(@RequestParam("mentorMemberId") String mentorMemberId,
                              @RequestParam("menteeMemberId") String menteeMemberId) {
        messageService.acceptMenteeApply(mentorMemberId, menteeMemberId);
        return "redirect:/message/list";
    }

    // @PostMapping("/rejectMentee")
    // public String rejectMentee(@RequestParam("mentorMemberId") String mentorMemberId,
    //                           @RequestParam("menteeMemberId") String menteeMemberId) {
    //     messageService.rejectMenteeApply(mentorMemberId, menteeMemberId);
    //     return "redirect:/message/list";
    // }

}
