package com.teamProject.lostArkProject.teaching.controller;


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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class TeachingController {

    @Autowired
    private TeachingService teachingService;

    @GetMapping("/teaching/newMentor")
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


    @PostMapping("/teaching/newMentor")
    public String newMentor(@ModelAttribute MentorDTO mentorDTO, @RequestParam("mentorContentId[]") String[] contentIds) {
        // 배열로 받은 mentorContentId를 하나의 문자열로 변환
        String joinedContentIds = String.join(", ", contentIds);
        // DTO에 문자열로 저장
        mentorDTO.setMentorContentId(joinedContentIds);
        // 서비스 레이어를 통해 데이터베이스에 저장
        teachingService.newMentor(mentorDTO);
        return "redirect:/teaching/mentorList";
    }

    @GetMapping("/teaching/mentorList")
    public String mentorList(HttpSession session, Model model) {
        Object member = session.getAttribute("member");
        if (member == null) {
            // 세션에 "member" 객체가 없으면 접근 불가
            return "redirect:/member/signin"; // 로그인 페이지로 리다이렉트
        }
        List<MentorListDTO> mentors = teachingService.getMentorList();
        model.addAttribute("mentors", mentors);
        return "teaching/mentorList";
    }

    @GetMapping("/teaching/mentorListDetail/{mentorMemberId}")
    public String mentorListDetail(@PathVariable("mentorMemberId") String mentorMemberId, Model model) {
        List<MentorListDTO> mentors = teachingService.getMentorDetail(mentorMemberId);
        model.addAttribute("mentors", mentors);
        //아이디로 상세정보가져오기
        return "teaching/mentorListDetail";
    }

    // 멘티 신청 상태 + ACCEPTED 시 디스코드 ID 포함
    @GetMapping("/teaching/mentee/apply-status-detail/{menteeMemberId}")
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



    @PostMapping("/teaching/applyMentee")
    public String applyMentee(@RequestParam("mentorMemberId") String mentorId,
                              @RequestParam("menteeMemberId") String menteeId) {
        MenteeApplyDTO dto = new MenteeApplyDTO();
        dto.setMentor_member_id(mentorId);
        dto.setMentee_member_id(menteeId);
        dto.setApply_status("REQUESTED");

        teachingService.insertMenteeApply(dto); // 서비스 통해 DAO 호출

        return "redirect:/teaching/mentorList"; // 신청 후 리스트로 리디렉션
    }
// sse 시도
/*

    @Autowired
    private NotificationService notificationService;

    // ======================
    // 멘토용 구독 엔드포인트
    // ======================
    @GetMapping(value = "/teaching/subscribe/mentor/{mentorMemberId}", produces = "text/event-stream")
    public SseEmitter subscribeMentor(@PathVariable String mentorMemberId) {
        return notificationService.subscribeMentor(mentorMemberId);
    }

    // ======================
    // 멘티용 구독 엔드포인트
    // ======================
    @GetMapping(value = "/teaching/subscribe/mentee/{menteeMemberId}", produces = "text/event-stream")
    public SseEmitter subscribeMentee(@PathVariable String menteeMemberId) {
        return notificationService.subscribeMentee(menteeMemberId);
    }


    */
/**
     * 멘티가 신청을 하면, 멘토에게 알림을 보낼 수 있는 예시 (선택 구현)
     *//*

    @PostMapping("/teaching/applyMentee")
    public String applyMentee(@RequestParam("mentorMemberId") String mentorMemberId,
                              @RequestParam("menteeMemberId") String menteeMemberId) {
        // DB에 신청 정보 저장
        // teachingService.applyMentee(mentorId, menteeId); // 가정

        // SSE로 "새로운 멘티 신청" 알림을 멘토에게 전송
        notificationService.sendNotificationToMentor(mentorMemberId, "새로운 신청이 있습니다.", menteeMemberId);

        return "redirect:/somePage";
    }

    */
/**
     * 멘토가 멘티 신청을 수락하는 경우 -> 멘티에게 '수락됨' + '멘토 디스코드' 를 전송
     *//*

    @PostMapping("/teaching/acceptMentee")
    public String acceptMenteeSubmit(@RequestParam("mentorMemberId") String mentorMemberId,
                                     @RequestParam("menteeMemberId") String menteeMemberId) {
        // (1) DB 상에서 멘티 수락 처리
        teachingService.acceptMentee(menteeMemberId, mentorMemberId);

        // (2) 멘토 디스코드 ID를 조회
        String mentorDiscordId = teachingService.getMentorDiscordId(mentorMemberId);

        // (3) SSE로 알림(멘토 디스코드 ID)을 멘티에게 전송
        notificationService.sendNotificationToMentee(
                menteeMemberId,
                "디스코드 아이디를 수락합니다.",
                mentorDiscordId
        );

        return "redirect:/member/myPage";
    }
*/





}
