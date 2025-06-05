package com.teamProject.lostArkProject.teaching.dao;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MessageDAO {
    MenteeApplyDTO getMenteeApplyDetail(Map<String, Object> param);
    void acceptMenteeApply(MenteeApplyDTO menteeApplyDTO);
    void rejectMenteeApply(MenteeApplyDTO menteeApplyDTO);
    List<MenteeDTO> getMenteeDetail(String menteeMemberId);
    Map<String, Object> getMenteeCharacterInfo(String menteeMemberId);
}
