package com.teamProject.lostArkProject.teaching.dao;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import org.apache.ibatis.annotations.Mapper;
import com.teamProject.lostArkProject.teaching.dto.MentorDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


@Mapper
public interface TeachingDAO {
    public void newMentor(MentorDTO mentorDTO);
    void insertMentorContent(@Param("mentorMemberId") String mentorMemberId, @Param("mentorContentId") String contentId);
    public void newMentee(MenteeDTO menteeDTO);
    public List<Map<String,Object>> getMentorList();
    public List<Map<String,Object>> getMentorContent();
    public List<Map<String,Object>> getMemberCharacter();
//    public Map<String,Object> getMentorListDetail(long mentorId);
    String findDiscordIdByMentorId(@Param("mentorMemberId") String mentorMemberId);

    //sse
    // 1) 멘티가 멘토에게 신청 (INSERT)
    void insertMenteeApply(MenteeApplyDTO menteeApplyDTO);

    // 2) 멘토가 신청을 'ACCEPTED'로 (수락) 업데이트
    void acceptMenteeApply(MenteeApplyDTO menteeApplyDTO);

    // 3) 멘토가 신청을 'REJECTED'로 (거절) 업데이트
    void rejectMenteeApply(MenteeApplyDTO menteeApplyDTO);
}
