package com.teamProject.lostArkProject.notice.controller;

import com.teamProject.lostArkProject.common.dto.PaginatedRequestDTO;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.notice.domain.Notice;
import com.teamProject.lostArkProject.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class NoticeRestController {
    private final NoticeService noticeService;

    @GetMapping("/notices")
    public ResponseEntity<PaginatedResponseDTO<Notice>> getNoticeList(@ModelAttribute PaginatedRequestDTO requestDTO) {
        PaginatedResponseDTO<Notice> res = noticeService.getNoticeList(requestDTO);

        return ResponseEntity.ok(res);
    }
}
