package com.teamProject.lostArkProject.teaching.service;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import java.util.List;
import java.util.Map;

public interface MessageService {
    public void acceptMentee(String menteeMemberId, String mentorDiscordId);
    MenteeApplyDTO getMenteeApplyDetail(Map<String, Object> param);
    List<MenteeDTO> getMenteeDetail(String menteeMemberId);
    Map<String, Object> getMenteeCharacterInfo(String menteeMemberId);
    void acceptMenteeApply(String mentorMemberId, String menteeMemberId);
    void rejectMenteeApply(String mentorMemberId, String menteeMemberId);
    List<Map<String, Object>> getAllMenteeAppliesByMentor(String mentorMemberId);
    List<Map<String, Object>> getAllMenteeAppliesByMentee(String menteeMemberId);
}
