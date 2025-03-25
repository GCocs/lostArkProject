package com.teamProject.lostArkProject.teaching.controller;

import com.teamProject.lostArkProject.teaching.dto.MentorDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorListDTO;
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

    @GetMapping("/teaching/acceptMentee/{menteeId}")
    public String acceptMenteeForm(@PathVariable("menteeId") Long menteeId,
                                   Model model) {
        // 멘티 ID로 필요한 정보를 DB에서 조회해 모델에 넣거나,
        // 단순히 menteeId만 넘길 수도 있음.
        model.addAttribute("menteeMemeberId", menteeId);

        // templates 폴더 아래 acceptMentee.html (또는 다른 이름)로 렌더링
        return "teaching/acceptMentee";
    }



    // SSE emitters를 관리하기 위한 ConcurrentHashMap 추가
    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    // SSE 연결 유지 시간 설정 (30분)
    private static final Long DEFAULT_TIMEOUT = 1800000L;

    // SSE 구독 Endpoint 추가
    @GetMapping(value = "/teaching/subscribe/{userId}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        sseEmitters.put(userId, emitter);

        emitter.onCompletion(() -> sseEmitters.remove(userId));
        emitter.onTimeout(() -> sseEmitters.remove(userId));
        emitter.onError(e -> sseEmitters.remove(userId));

        // 연결 초기화용 더미 데이터 전송
        try {
            emitter.send(SseEmitter.event().name("INIT").data("connected"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    // 멘티 신청 수락 처리 및 SSE 알림 전송을 함께 진행하는 메서드
    @PostMapping("/teaching/acceptMentee")
    public String acceptMenteeSubmit(@RequestParam("menteeMemberId") Long menteeMemberId, HttpSession session) {

        // (1) 멘티 수락 처리 로직 추가
        // 예시로, 세션에서 멘토의 DiscordId를 가져와 멘티에게 전송하는 방식입니다.
        Object mentor = session.getAttribute("member");
        String mentorDiscordId = "[멘토의 Discord ID]"; // mentor 객체에서 discordId 가져오기
        teachingService.acceptMentee(menteeMemberId, mentorDiscordId);

        // (2) SSE로 알림을 전송하는 로직 추가
        sendNotificationToClient(menteeMemberId.toString(), "MENTEE_ACCEPTED", mentorDiscordId);

        return "redirect:/member/myPage";
    }

    // SSE 알림 전송 메서드 추가
    private void sendNotificationToClient(String userId, String eventName, String data) {
        SseEmitter emitter = sseEmitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
                sseEmitters.remove(userId);
            }
        }
    }
}
