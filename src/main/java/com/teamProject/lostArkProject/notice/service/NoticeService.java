package com.teamProject.lostArkProject.notice.service;

import com.teamProject.lostArkProject.common.dto.PaginatedRequestDTO;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.common.dto.Pagination;
import com.teamProject.lostArkProject.notice.dao.NoticeDAO;
import com.teamProject.lostArkProject.notice.domain.Notice;
import com.teamProject.lostArkProject.notice.dto.NoticeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class NoticeService {
    private final NoticeDAO noticeDAO;

    public PaginatedResponseDTO<NoticeDTO> getNoticeList(PaginatedRequestDTO requestDTO) {
        List<Notice> noticeList = noticeDAO.getNoticeList(requestDTO);
        List<NoticeDTO> noticeDTOList = noticeList.stream()
                .map(NoticeDTO::new)
                .toList();

        int totalCount = noticeDAO.getTotalCount();
        Pagination pagination = new Pagination(requestDTO.getPage(), requestDTO.getSize(), totalCount);

        return new PaginatedResponseDTO<>(noticeDTOList, pagination);
    }
}
