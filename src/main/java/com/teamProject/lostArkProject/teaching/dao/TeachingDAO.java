package com.teamProject.lostArkProject.teaching.dao;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import org.apache.ibatis.annotations.Mapper;
import com.teamProject.lostArkProject.teaching.dto.MentorDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Mapper
public interface TeachingDAO {
    public void newMentor(MentorDTO mentorDTO);
    void insertMentorContent(@Param("mentorMemberId") String mentorMemberId, @Param("mentorContentId") String contentId);
    public void newMentee(MenteeDTO menteeDTO);
    public List<Map<String,Object>> getMentorList();
    public List<Map<String,Object>> getMentorContent();
    public List<Map<String,Object>> getMemberCharacter();
//    public Map<String,Object> getMentorListDetail(long mentorId);
    String findDiscordIdByMentorId(String mentorMemberId);
    // 멘토의 모든 신청 상태 조회 (REQUESTED, ACCEPTED, REJECTED)
    List<Map<String, Object>> getApplyStatusByMentee(String mentorMemberId);
    //sse
    void insertMenteeApply(MenteeApplyDTO menteeApplyDTO);
    int isDuplicateMenteeApply(MenteeApplyDTO dto);
    List<Map<String, Object>> getRequestedAppliesByMentor(String mentorMemberId);
    Set<String> getAppliedMentorIdsByMentee(String menteeId);
    List<Map<String, Object>> getAppliedMentorStatusByMentee(String menteeId);
    boolean isBlockedMentee(@Param("mentorMemberId") String mentorMemberId, @Param("menteeMemberId") String menteeMemberId);
    Map<String, Object> getMenteeApplyInfo(@Param("mentorMemberId") String mentorMemberId, @Param("menteeMemberId") String menteeMemberId);
    List<Map<String, Object>> getExpiredRejectedApplies();
    void deleteRejectedApply(@Param("mentorMemberId") String mentorMemberId, @Param("menteeMemberId") String menteeMemberId);
    Map<String, Object> getMentorInfoById(String mentorMemberId);
    List<String> getMentorContentIdsById(String mentorMemberId);
    int isMentorExists(String mentorMemberId);
    // 멘토 정보 수정
    void updateMentor(MentorDTO mentorDTO);
    void deleteMentorContent(String mentorMemberId);
}
