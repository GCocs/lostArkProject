package com.teamProject.lostArkProject.teaching.dao;

import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageDAO {
    MenteeApplyDTO getMenteeApplyDetail(String menteeMemberId);
    void acceptMenteeApply(MenteeApplyDTO menteeApplyDTO);
    void rejectMenteeApply(MenteeApplyDTO menteeApplyDTO);
}
