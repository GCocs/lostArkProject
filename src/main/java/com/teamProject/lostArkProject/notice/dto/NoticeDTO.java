package com.teamProject.lostArkProject.notice.dto;

import com.teamProject.lostArkProject.notice.domain.Notice;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Schema(description = "공지 Dto")
public class NoticeDTO {

    @Schema(description = "공지 인덱스", example = "3")
    private int noticeNumber;

    @Schema(description = "공지 제목", example = "공지사항")
    private String title;

    @Schema(description = "공지 내용", example = "공지사항 내용")
    private String content;

    @Schema(description = "공지 이미지 경로", example = "")
    private String image;

    @Schema(description = "공지 작성일", example = "2025-07-16T14:00:00.00")
    private LocalDateTime createdAt;

    public NoticeDTO(Notice notice) {
        noticeNumber = notice.getNoticeNumber();
        title = notice.getTitle();
        content = notice.getContent();
        image = notice.getImage();
        createdAt = notice.getCreatedAt();
    }
}
