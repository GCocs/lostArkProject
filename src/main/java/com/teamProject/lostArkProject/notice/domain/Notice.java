package com.teamProject.lostArkProject.notice.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Alias("notice")
public class Notice {
    private int noticeNumber;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
}

