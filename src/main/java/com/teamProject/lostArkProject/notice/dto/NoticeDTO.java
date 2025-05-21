package com.teamProject.lostArkProject.notice.dto;

import com.teamProject.lostArkProject.notice.domain.Notice;
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

    public NoticeDTO(Notice notice) {
        noticeNumber = notice.getNoticeNumber();
        title = notice.getTitle();
        content = notice.getContent();
        image = notice.getImage();
        createdAt = notice.getCreatedAt();
    }
}
