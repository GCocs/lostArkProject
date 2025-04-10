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
    // 멘티의 신청 상태 + 멘토 ID 조회
    List<Map<String, Object>> getApplyStatusByMentee(@Param("menteeMemberId") String menteeMemberId);
    //sse
    void insertMenteeApply(MenteeApplyDTO menteeApplyDTO);
    void acceptMenteeApply(MenteeApplyDTO menteeApplyDTO);
    void rejectMenteeApply(MenteeApplyDTO menteeApplyDTO);
    int isDuplicateMenteeApply(MenteeApplyDTO dto);

}
