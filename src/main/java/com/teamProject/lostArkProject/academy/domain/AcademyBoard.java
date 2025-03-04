package com.teamProject.lostArkProject.academy.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDateTime;

@Getter
@Setter
@Alias("academy_board")
public class AcademyBoard {
    private int academyBoardNumber;
    private String writer;
    private String title;
    private String content;
    private String image;
    private LocalDateTime createdAt;
}
