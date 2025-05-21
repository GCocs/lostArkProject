package com.teamProject.lostArkProject.main.controller;

import com.teamProject.lostArkProject.alarm.domain.Alarm;
import com.teamProject.lostArkProject.alarm.service.AlarmService;
import com.teamProject.lostArkProject.collectible.domain.RecommendCollectible;
import com.teamProject.lostArkProject.collectible.domain.RecommendCollectibleFullDTO;
import com.teamProject.lostArkProject.collectible.dto.CollectiblePointSummaryDTO;
import com.teamProject.lostArkProject.collectible.service.CollectibleService;
import com.teamProject.lostArkProject.common.dto.PaginatedRequestDTO;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.notice.dto.NoticeDTO;
import com.teamProject.lostArkProject.notice.service.NoticeService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MainController {
    private final CollectibleService collectibleService;
    private final AlarmService alarmService;
    private final NoticeService noticeService;

    // 메인페이지
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        PaginatedRequestDTO requestDTO = new PaginatedRequestDTO(1, 3);
        PaginatedResponseDTO<NoticeDTO> noticeRes = noticeService.getNoticeList(requestDTO);

        Member member = (Member) session.getAttribute("member");
        if (member != null) {
            List<Alarm> alarms = alarmService.getAllAlarm(member.getMemberId());
            log.info("alarms: {}", alarms);
            model.addAttribute("alarms", alarms);
        }
        model.addAttribute("noticeList", noticeRes.getData());

        return "index";
    }

    // 내실
    @GetMapping("/collectible")
    public String getCharacterCollectible(Model model, HttpSession session) {
        Member member = (Member) session.getAttribute("member"); // http 세션에서 가져온 닉네임
        if(member == null) {
            return "member/signin";
        }
        List<CollectiblePointSummaryDTO> collectibleItemList = collectibleService.getCollectiblePointSummary(member.getMemberId());
        model.addAttribute("collectibleItemList", collectibleItemList);
        List<RecommendCollectible> recommendCollectibleList = collectibleService.getRecommendCollectible(member.getMemberId());
        model.addAttribute("recommendCollectibleList", recommendCollectibleList);
        List<RecommendCollectibleFullDTO> recommendCollectibleFullList = collectibleService.getRecommendFullCollectible(member.getMemberId());
        model.addAttribute("recommendCollectibleFullList", recommendCollectibleFullList);
        return "collectible/collectible"; // 결과 뷰로 이동
    }

    @PostMapping("/collectible/clear")
    public String clearCollectible(
            @RequestParam("collectibleId") int collectibleId,
            @SessionAttribute("member") Member member) {

        collectibleService.clearCollectible(member.getMemberId(), collectibleId);
        return "redirect:/collectible";
    }
}