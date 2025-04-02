package com.teamProject.lostArkProject.teaching.service;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorDTO;
import com.teamProject.lostArkProject.teaching.dto.MentorListDTO;

import java.util.List;
import java.util.Map;


public interface TeachingService {
    public void newMentor(MentorDTO mentorDTO);
    public void newMentee(MenteeDTO menteeDTO);
    public List<MentorListDTO> getMentorList();
    public List<MentorListDTO> getMentorDetail(String mentorMemberId);
    public void acceptMentee(String menteeMemberId, String mentorDiscordId);
    String getMentorDiscordId(String mentorMemberId);
    List<Map<String, Object>> getApplyStatusByMentee(String menteeMemberId);
    void insertMenteeApply(MenteeApplyDTO menteeApplyDTOdto);


}
