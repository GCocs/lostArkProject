package com.teamProject.lostArkProject.member.controller;

import com.teamProject.lostArkProject.member.dto.EmailDTO;
import com.teamProject.lostArkProject.member.service.EmailService;
import com.teamProject.lostArkProject.member.service.MemberService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;
    private final MemberService memberService;

    @ResponseBody
    @PostMapping("/send-email")
    public void emailCheck(HttpServletRequest request, @RequestBody Map<String, String> requestMap) throws MessagingException, UnsupportedEncodingException {
        String authCode = emailService.sendSimpleMessage(requestMap.get("email"));
        emailService.saveAuthCode(requestMap.get("email"), authCode);

        HttpSession session = request.getSession();
        session.setAttribute("checkCode", authCode);
        session.setMaxInactiveInterval(600); // 10 * 60 = 10분
    }
}