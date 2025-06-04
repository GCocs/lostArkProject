package com.teamProject.lostArkProject.teaching.service;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import java.util.List;

public interface MessageService {
    public void acceptMentee(String menteeMemberId, String mentorDiscordId);
    MenteeApplyDTO getMenteeApplyDetail(String menteeMemberId);
    List<MenteeDTO> getMenteeDetail(String menteeMemberId);
}
