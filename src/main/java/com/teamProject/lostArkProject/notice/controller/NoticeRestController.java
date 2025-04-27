package com.teamProject.lostArkProject.notice.controller;

import com.teamProject.lostArkProject.common.dto.PaginatedRequestDTO;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.notice.dto.NoticeDTO;
import com.teamProject.lostArkProject.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class NoticeRestController {
    private final NoticeService noticeService;

    @GetMapping("/notices")
    public ResponseEntity<PaginatedResponseDTO<NoticeDTO>> getNoticeList(@ModelAttribute PaginatedRequestDTO requestDTO) {
        PaginatedResponseDTO<NoticeDTO> res = noticeService.getNoticeList(requestDTO);

        return ResponseEntity.ok(res);
    }
}
