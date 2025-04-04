package com.teamProject.lostArkProject.notice.service;

import com.teamProject.lostArkProject.common.dto.PaginatedRequestDTO;
import com.teamProject.lostArkProject.notice.dao.NoticeDAO;
import com.teamProject.lostArkProject.notice.domain.Notice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    private final NoticeDAO noticeDAO;

    public List<Notice> getNoticeList(PaginatedRequestDTO requestDTO) {
        List<Notice> noticeList = noticeDAO.getNoticeList(requestDTO);
        return noticeList;
    }
}
