package com.teamProject.lostArkProject.teaching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import com.teamProject.lostArkProject.teaching.service.MessageService;
import java.util.List;


@Controller
@RequestMapping("/message")
public class MessageController {
    
    @Autowired
    private MessageService messageService;

    @GetMapping("/newMessageDetail")
    public String newMessageDetail(@RequestParam("menteeMemberId") String menteeMemberId, Model model) {
        MenteeApplyDTO apply = messageService.getMenteeApplyDetail(menteeMemberId);
        List<MenteeDTO> menteeList = messageService.getMenteeDetail(menteeMemberId);
        model.addAttribute("apply", apply);
        model.addAttribute("menteeList", menteeList);
        model.addAttribute("menteeMemberId", menteeMemberId);
        return "message/newMessageDetail";
    }


    @GetMapping("/list")
    public String getMessagelist() {
        return "message/list";
    }

}
