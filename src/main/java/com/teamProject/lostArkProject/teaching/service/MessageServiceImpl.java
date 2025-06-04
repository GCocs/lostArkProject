package com.teamProject.lostArkProject.teaching.service;

import com.teamProject.lostArkProject.teaching.dao.MessageDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import com.teamProject.lostArkProject.teaching.dto.MenteeDTO;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageDAO messageDAO;

    @Override
    public void acceptMentee(String menteeMemberId, String mentorDiscordId) {

    }

    @Override
    public MenteeApplyDTO getMenteeApplyDetail(String menteeMemberId) {
        return messageDAO.getMenteeApplyDetail(menteeMemberId);
    }

    @Override
    public List<MenteeDTO> getMenteeDetail(String menteeMemberId) {
        return messageDAO.getMenteeDetail(menteeMemberId);
    }

}
