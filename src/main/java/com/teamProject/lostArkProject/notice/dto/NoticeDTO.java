package com.teamProject.lostArkProject.notice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NoticeDTO {
    private int noticeNumber;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
}
