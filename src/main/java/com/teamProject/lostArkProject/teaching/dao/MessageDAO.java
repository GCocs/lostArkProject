package com.teamProject.lostArkProject.teaching.dao;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageDAO {
    MenteeApplyDTO getMenteeApplyDetail(String menteeMemberId);
    void acceptMenteeApply(MenteeApplyDTO menteeApplyDTO);
    void rejectMenteeApply(MenteeApplyDTO menteeApplyDTO);
    List<MenteeDTO> getMenteeDetail(String menteeMemberId);
}
