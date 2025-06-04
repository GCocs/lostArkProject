package com.teamProject.lostArkProject.teaching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/message")
public class MessageController {
    

    @GetMapping("/newMessageDetail")
    public String newMessageDetail() {

        
        return "message/newMessageDetail";
    }


    @GetMapping("/list")
    public String getMessagelist() {
        return "message/list";
    }

}
