package com.teamProject.lostArkProject.teaching.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MenteeApplyDTO {
    private Long applyId;
    private String mentorMemberId;
    private String menteeMemberId;
    private String applyStatus; // REQUESTED, ACCEPTED, REJECTED 등
    private String rejectedReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
