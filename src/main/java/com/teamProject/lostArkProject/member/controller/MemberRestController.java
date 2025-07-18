package com.teamProject.lostArkProject.member.controller;

import com.teamProject.lostArkProject.collectible.domain.CharacterInfo;
import com.teamProject.lostArkProject.collectible.service.CollectibleService;
import com.teamProject.lostArkProject.member.domain.Member;
import com.teamProject.lostArkProject.member.domain.MemberCharacter;
import com.teamProject.lostArkProject.member.dto.CertificationDTO;
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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@RestController
@RequestMapping ("/member")
@Slf4j
public class MemberRestController {
    private final MemberService memberService;
    private final CollectibleService collectibleService;
    private static final String EMAIL_REGEX =
            "^[0-9A-Za-z]([-_.]?[0-9A-Za-z])*@[0-9A-Za-z]([-_.]?[0-9A-Za-z])*\\.[A-Za-z]{2,3}$";

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);

    private static final String PASSWORD_REGEX =
            "^(?=.*[A-Za-z])(?=.*[0-9]).+$";

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile(PASSWORD_REGEX);

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

        //유효성검사
        if (requestMap.get("email") == null) return false;
        Matcher matcher = EMAIL_PATTERN.matcher(requestMap.get("email"));
        if (!matcher.matches()) return false;
        if (requestMap.get("PW") == null) return false;
        Matcher matcher2 = PASSWORD_PATTERN.matcher(requestMap.get("PW"));
        if (!matcher2.matches()) return false;




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
        return certificationDTOMono.doOnNext(certificationDTO -> {
                    List<String> requiredEquipmentList = certificationDTO.getEquipment()
                            .entrySet()
                            .stream()
                            .filter(entry -> entry.getValue().isUnequippedRequired())
                            .map(entry -> entry.getKey())
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

    @GetMapping("/{nickname}/checkCertification")
    public boolean checkCertification(@PathVariable("nickname") String nickname, HttpSession session)  {
        List<String> excludedList =
                (List<String>) session.getAttribute("requiredEquipmentList");
        if (excludedList == null) {
            return false;
        }

        Set<String> excludedTypes = new HashSet<>(excludedList);

        Mono<List<CertificationDTO>> equipmentList = memberService.getCertification(nickname);

        List<CertificationDTO> certList = equipmentList.block();
        certList.forEach(c -> System.out.println("Certification type = " + c.getType()));

        Set<String> expectedTypes = Set.of(
                "무기", "투구", "상의", "하의", "장갑",
                "어깨", "목걸이", "귀걸이1", "귀걸이2",
                "반지1", "반지2", "어빌리티 스톤", "팔찌", "나침반"
        );

        Set<String> actualTypes = certList.stream()
                .map(CertificationDTO::getType)
                .collect(Collectors.toSet());

        if (excludedTypes.contains("귀걸이1") && !excludedTypes.contains("귀걸이2")) {
            excludedTypes = excludedTypes.stream()
                    .map(type -> type.equals("귀걸이1") ? "귀걸이2" : type)
                    .collect(Collectors.toSet());
        }

        if (excludedTypes.contains("반지1") && !excludedTypes.contains("반지2")) {
            excludedTypes = excludedTypes.stream()
                    .map(type -> type.equals("반지1") ? "반지2" : type)
                    .collect(Collectors.toSet());
        }

        System.out.println("first = " + actualTypes);

        Set<String> intersection = new HashSet<>(actualTypes);
        intersection.retainAll(excludedTypes);
        if (!intersection.isEmpty()) {
            System.out.println("중복된 excluded 타입 발견: " + intersection);
            return false;
        }

        actualTypes.addAll(excludedTypes);

        System.out.println("excluded = " + excludedTypes);
        System.out.println("expected = " + expectedTypes);
        System.out.println("restored = " + actualTypes);

        return actualTypes.equals(expectedTypes);

    }

    @PostMapping("finishCertification")
    public void finishCertification(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Member member = (Member) session.getAttribute("member");

        memberService.updateCertification(member.getMemberId());
    }

}
