package com.teamProject.lostArkProject.content.controller;

import com.teamProject.lostArkProject.content.dto.ContentDTO;
import com.teamProject.lostArkProject.content.dto.ContentStartTimeDTO;
import com.teamProject.lostArkProject.content.service.ContentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "컨텐츠 API", description = "ContentRestController")
public class ContentRestController {
    private final ContentService contentService;

    @Operation(summary = "컨텐츠 조회", description = "데이터베이스에서 컨텐츠 데이터를 조회합니다.")
    @GetMapping("/contents")
    public ResponseEntity<List<ContentDTO>> getContentsAll() {
        return ResponseEntity.ok(contentService.getContentsAll());
    }

    @Operation(summary = "컨텐츠 시작시간 조회", description = "데이터베이스에서 컨텐츠 데이터와 시작시간 데이터를 조회합니다.")
    @GetMapping("/contents/start-time")
    public ResponseEntity<List<ContentStartTimeDTO>> getContentStartTimes() {
        return ResponseEntity.ok(contentService.getContentStartTimes());
    }
}
