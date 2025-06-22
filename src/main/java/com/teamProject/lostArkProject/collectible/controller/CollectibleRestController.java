package com.teamProject.lostArkProject.collectible.controller;

import com.teamProject.lostArkProject.collectible.domain.CharacterInfo;
import com.teamProject.lostArkProject.collectible.domain.RecommendCollectible;
import com.teamProject.lostArkProject.collectible.dto.CollectiblePointSummaryDTO;
import com.teamProject.lostArkProject.collectible.dto.RecommendCollectibleFullDTO;
import com.teamProject.lostArkProject.collectible.service.CollectibleService;
import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
public class CollectibleRestController {

    private final CollectibleService collectibleService;
    private final MemberService memberService;

    public CollectibleRestController(CollectibleService collectibleService, MemberService memberService) {
        this.collectibleService = collectibleService;
        this.memberService = memberService;
    }

    @GetMapping("/characters/{characterName}/siblings")
    public Mono<List<CharacterInfo>> getCharacterInfo(@PathVariable String characterName) {
        return memberService.getCharacterInfo(characterName);
    }

    // collectibles url로 GET 요청 시 json 데이터 반환
    @GetMapping("/collectibles")
    public List<CollectiblePointSummaryDTO> getCharacterCollectable(HttpSession session) {
        Member member = (Member) session.getAttribute("member"); // http 세션에서 가져온 닉네임
        return collectibleService.getCollectiblePointSummary(member.getMemberId());
    }

    @PostMapping("/collectible/clear-status")
    public ResponseEntity<Void> updateClearStatus(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        boolean ok = collectibleService.updateCleared(
                Integer.parseInt(requestMap.get("recommendCollectibleID")),
                Boolean.parseBoolean(requestMap.get("cleared")),
                member.getMemberId()
        );
        if (ok) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/collectible/getRecommendCollectibleList")
    public List<RecommendCollectible> getRecommendCollectibleList(HttpSession session){
        Member member = (Member) session.getAttribute("member");
        return collectibleService.getRecommendCollectible(member.getMemberId());
    }

    @PostMapping("/collectible/getRecommendFullCollectibleList")
    public List<RecommendCollectibleFullDTO> getRecommendFullCollectibleList(HttpSession session){
        Member member = (Member) session.getAttribute("member");
        return collectibleService.getRecommendFullCollectible(member.getMemberId());
    }

    @PostMapping("/collectible/clear")
    public void clearCollectible(HttpSession session, @RequestBody Map<String, String> requestMap) {
        Member member = (Member) session.getAttribute("member");
        int collectibleId = Integer.parseInt(requestMap.get("collectibleId"));
        collectibleService.clearCollectible(member.getMemberId(), collectibleId);
    }

    public static class ClearStatusRequest {
        private int recommendCollectibleID;
        private boolean cleared;
        // getters/setters
    }

}