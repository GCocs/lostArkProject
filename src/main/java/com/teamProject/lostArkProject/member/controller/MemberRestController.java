package com.teamProject.lostArkProject.member.controller;

import com.teamProject.lostArkProject.collectible.domain.CharacterInfo;
import com.teamProject.lostArkProject.collectible.service.CollectibleService;
import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.member.domain.MemberCharacter;
import com.teamProject.lostArkProject.member.dto.CharacterCertificationDTO;
import com.teamProject.lostArkProject.member.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("/member")
@Slf4j
public class MemberRestController {
    private final MemberService memberService;
    private final CollectibleService collectibleService;

    public MemberRestController(MemberService memberService, CollectibleService collectibleService) {
        this.memberService = memberService;
        this.collectibleService = collectibleService;
    }

    @PostMapping("/check-email")
    public boolean checkEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        return memberService.checkEmail(email);
    }

    //대표캐릭터 api 체크
    @PostMapping("/check-representativeCharacter")
    public boolean checkRepresentativeCharacter(@RequestBody Map<String, String> request) {
        String representativeCharacter = request.get("representativeCharacter");
        System.out.println(memberService.getCharacterInfo(representativeCharacter));
        return false;
    }

    //회원가입
    @PostMapping("/signup-process")
    public boolean signupProcess(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        HttpSession session = request.getSession();
        List<CharacterInfo> characterInfoList = memberService.getCharacterInfo(requestMap.get("representativeCharacter"))
                .block();
        String rosterLevel = memberService.getRosterLevel(requestMap.get("representativeCharacter")).block();
        System.out.println(rosterLevel);
        if (characterInfoList == null || characterInfoList.isEmpty()) {
            System.out.println("회원가입 실패: 대표 캐릭터가 유효하지 않습니다.");
            return false; // 검증 실패
        }

        List<MemberCharacter> memberCharacterList = memberService.getMemberCharacterList(characterInfoList, rosterLevel, requestMap.get("email"));

        Member member = new Member();
        member.setMemberId(requestMap.get("email"));
        member.setMemberPasswd(requestMap.get("PW"));
        member.setRepresentativeCharacterNickname(requestMap.get("representativeCharacter"));

        Member sessionMember = new Member();
        sessionMember.setMemberId(requestMap.get("email"));
        sessionMember.setRepresentativeCharacterNickname(requestMap.get("representativeCharacter"));

        memberService.signupMember(member);
        memberService.insertMemberCharacter(memberCharacterList);
        session.setAttribute("member", sessionMember);
        collectibleService.saveCollectiblePoint(requestMap.get("representativeCharacter"), requestMap.get("email"));
        return true;
    }

    //로그인
    @PostMapping("/signin-process")
    public boolean signinProcess(HttpServletRequest request, @RequestBody Map<String, String> requestMap,
                                 HttpServletResponse response) {
        HttpSession session = request.getSession();
        if (memberService.checkSignin(requestMap.get("email"),requestMap.get("PW"))) {
            String memberNickname = memberService.getRepresentativeCharacterNickname(requestMap.get("email"));

            Member sessionMember = new Member();
            sessionMember.setMemberId(requestMap.get("email"));
            sessionMember.setRepresentativeCharacterNickname(memberNickname);

            session.setAttribute("member", sessionMember);

            //아이디 저장
            if(("true").equals(requestMap.get("saveId"))) {
                Cookie cookie = new Cookie("saveId", requestMap.get("email"));
                response.addCookie(cookie);
            } else {
                Cookie cookie = new Cookie("saveId", null);
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
            return true;
        }
        return false;
    }
    @PostMapping("/changePassword-process")
    public boolean changePasswordProcess(@RequestBody Map<String, String> requestMap) {
        memberService.changePassword(requestMap.get("email"),requestMap.get("PW"));
        return true;
    }

    @PostMapping("/check-auth")
    public String checkAuth(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        HttpSession session = request.getSession();

        if (session.getAttribute("checkCode") == null) {
            return "expiration";
        }

        if (requestMap.get("authCode").equals((String) session.getAttribute("checkCode"))) {
            session.invalidate();
            session.setMaxInactiveInterval(3600); //1 * 60 * 60 1시간
            return "true";
        } else {
            return "false";
        }
    }

    @PostMapping("changeRCN")
    public boolean changeRCN(HttpServletRequest request, @RequestBody Map<String, String> requestMap) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        if(requestMap.get("RCN") == null || member.getRepresentativeCharacterNickname().equals(requestMap.get("RCN"))) return false;
        if(memberService.updateRCN(requestMap.get("email"), requestMap.get("RCN"))) {
            collectibleService.updateCollectible(requestMap.get("email"), requestMap.get("RCN"));
            Member sessionMember = new Member();
            sessionMember.setMemberId(requestMap.get("email"));
            sessionMember.setRepresentativeCharacterNickname(requestMap.get("RCN"));

            session.setAttribute("member", sessionMember);
            return true;
        }
        return false;
    }

    // 캐릭터 인증 요청
    @GetMapping("/{nickname}/certification")
    public Mono<CharacterCertificationDTO> getCharacterImage(@PathVariable("nickname") String nickname, HttpSession session) {
        // 예외 처리
        if (session.getAttribute("requiredEquipmentList") != null) {
            throw new RuntimeException("이미 인증을 요청하셨습니다.");
        }

        // api 데이터 받아옴
        Mono<CharacterCertificationDTO> certificationDTOMono = memberService.requestCertification(nickname);

        // 인증해야 하는 장비를 세션에 저장 후 반환 (구독)
        return certificationDTOMono.doOnNext(certificationDTO ->
                {
                    List<String> requiredEquipmentList = certificationDTO.getEquipment().values().stream()
                            .filter(equipmentDTO -> equipmentDTO.isUnequippedRequired())
                            .map(equipmentDTO -> equipmentDTO.getType())
                            .toList();
                    session.setAttribute("requiredEquipmentList", requiredEquipmentList);
                    log.info("세션에 저장된 장비: {}", session.getAttribute("requiredEquipmentList"));
                }
        );
    }

    // 캐릭터 인증 상태 초기화
    @DeleteMapping("/certification/reset")
    public ResponseEntity<String> resetCertificationState(HttpSession session) {
        // 예외 처리
        if (session.getAttribute("requiredEquipmentList") == null) {
            return ResponseEntity.ok("현재 진행 중인 캐릭터 인증이 없습니다.");
        }

        session.setAttribute("requiredEquipmentList", null);
        return ResponseEntity.ok("인증 초기화가 완료되었습니다.");
    }
}
