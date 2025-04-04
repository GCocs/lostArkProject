package com.teamProject.lostArkProject.collectible.controller;

import com.teamProject.lostArkProject.collectible.domain.CharacterInfo;
import com.teamProject.lostArkProject.collectible.dto.CollectiblePointSummaryDTO;
import com.teamProject.lostArkProject.collectible.service.CollectibleService;
import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.member.domain.MemberCharacter;
import com.teamProject.lostArkProject.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

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
}