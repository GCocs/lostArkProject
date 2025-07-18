package com.teamProject.lostArkProject.notice.controller;

import com.teamProject.lostArkProject.common.dto.PaginatedRequestDTO;
import com.teamProject.lostArkProject.common.dto.PaginatedResponseDTO;
import com.teamProject.lostArkProject.notice.dto.NoticeDTO;
import com.teamProject.lostArkProject.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
@Tag(name = "공지 RestAPI", description = "NoticeRestController")
public class NoticeRestController {
    private final NoticeService noticeService;

    @Operation(summary = "공지 목록 조회", description = "데이터베이스에서 공지 목록을 조회합니다.")
    @GetMapping("/notices")
    public ResponseEntity<PaginatedResponseDTO<NoticeDTO>> getNoticeList(@ModelAttribute PaginatedRequestDTO requestDTO) {
        PaginatedResponseDTO<NoticeDTO> res = noticeService.getNoticeList(requestDTO);

        return ResponseEntity.ok(res);
    }

    @Operation(summary = "특정 공지 조회", description = "데이터베이스에서 특정 공지를 조회합니다.")
    @GetMapping("/notices/{noticeNumber}")
    public ResponseEntity<NoticeDTO> getNoticeDetail(@PathVariable int noticeNumber) {
        NoticeDTO res = noticeService.getNoticeDetail(noticeNumber);

        return ResponseEntity.ok(res);
    }
}
