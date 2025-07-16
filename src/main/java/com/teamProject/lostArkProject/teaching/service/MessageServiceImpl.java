package com.teamProject.lostArkProject.teaching.service;

import com.teamProject.lostArkProject.teaching.dao.MessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO messageDAO;

    @Override
    public void acceptMentee(String menteeMemberId, String mentorDiscordId) {
        // 구현 내용
    }

    @Override
    public MenteeApplyDTO getMenteeApplyDetail(Map<String, Object> param) {
        return messageDAO.getMenteeApplyDetail(param);
    }

    @Override
    public List<MenteeDTO> getMenteeDetail(String menteeMemberId) {
        return messageDAO.getMenteeDetail(menteeMemberId);
    }

    @Override
    public Map<String, Object> getMenteeCharacterInfo(String menteeMemberId) {
        return messageDAO.getMenteeCharacterInfo(menteeMemberId);
    }

    @Override
    public void acceptMenteeApply(String mentorMemberId, String menteeMemberId) {
        MenteeApplyDTO dto = new MenteeApplyDTO();
        dto.setMentorMemberId(mentorMemberId);
        dto.setMenteeMemberId(menteeMemberId);
        messageDAO.acceptMenteeApply(dto);
    }

    @Override
    public void rejectMenteeApplyWithReason(String mentorMemberId, String menteeMemberId, String rejectReason, boolean blockMentee) {
        System.out.println("=== 거절 처리 시작 ===");
        System.out.println("멘토 ID: " + mentorMemberId);
        System.out.println("멘티 ID: " + menteeMemberId);
        System.out.println("거절 사유: " + rejectReason);
        System.out.println("차단 여부: " + blockMentee);
        
        // 1. 멘티 신청을 거절 사유와 함께 업데이트
        MenteeApplyDTO dto = new MenteeApplyDTO();
        dto.setMentorMemberId(mentorMemberId);
        dto.setMenteeMemberId(menteeMemberId);
        dto.setRejectedReason(rejectReason);
        
        System.out.println("DTO 생성 완료, 거절 쿼리 실행 중...");
        messageDAO.rejectMenteeApplyWithReason(dto);
        System.out.println("거절 쿼리 실행 완료");
        
        // 2. 차단 여부가 true인 경우 DISABLE_MENTEE 테이블에 추가
        if (blockMentee) {
            System.out.println("차단 처리 중...");
            Map<String, String> params = new HashMap<>();
            params.put("mentorMemberId", mentorMemberId);
            params.put("menteeMemberId", menteeMemberId);
            messageDAO.insertDisableMentee(params);
            System.out.println("차단 처리 완료");
        }
        
        System.out.println("=== 거절 처리 완료 ===");
    }

    @Override
    public List<Map<String, Object>> getAllMenteeAppliesByMentor(String mentorMemberId) {
        return messageDAO.getAllMenteeAppliesByMentor(mentorMemberId);
    }

    @Override
    public List<Map<String, Object>> getAllMenteeAppliesByMentee(String menteeMemberId) {
        return messageDAO.getAllMenteeAppliesByMentee(menteeMemberId);
    }




}
