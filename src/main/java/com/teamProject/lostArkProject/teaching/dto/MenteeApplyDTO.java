package com.teamProject.lostArkProject.teaching.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MenteeApplyDTO {
    private Long apply_id;
    private String mentor_member_id;
    private String mentee_member_id;
    private String apply_status; // REQUESTED, ACCEPTED, REJECTED 등
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
