package com.teamProject.lostArkProject.alarm.controller;

import com.teamProject.lostArkProject.alarm.domain.Alarm;
import com.teamProject.lostArkProject.alarm.service.AlarmService;
import com.teamProject.lostArkProject.common.exception.UnauthorizedException;
import com.teamProject.lostArkProject.common.utils.SessionUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@Slf4j
@Tag(name = "알림 API", description = "AlarmController")
public class AlarmController {
    private final AlarmService alarmService;

    @Operation(summary = "알림설정 조회", description = "로그인한 사용자가 설정한 알림 데이터를 조회합니다.")
    @GetMapping("/api/alarm")
    public ResponseEntity<List<Alarm>> getAllAlarm(HttpSession session) {
        String memberId = SessionUtils.getMemberId(session);

        if (memberId != null && !memberId.isEmpty()) {
            List<Alarm> alarms = alarmService.getAllAlarm(memberId);
            log.info("알람 데이터: {}", alarms);
            return ResponseEntity.ok(alarms);
        }

        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "알림 설정", description = "선택한 컨텐츠를 알림 테이블에 저장합니다.")
    @PostMapping("/api/alarm")
    public ResponseEntity<?> insertAlarm(HttpSession session,
                                         @RequestBody Alarm alarm,
                                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new IllegalArgumentException("유효하지 않은 입력입니다.");
        }

        String memberId = SessionUtils.getMemberId(session);

        if (memberId == null || memberId.isEmpty()) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        alarm.setMemberId(memberId);

        alarmService.insertAlarm(alarm);
        return ResponseEntity.ok("알림 설정에 성공했습니다.");
    }

    @Operation(summary = "알림 설정 해제", description = "선택한 컨텐츠를 알림 테이블에서 삭제합니다.")
    @DeleteMapping("/api/alarm/{contentName}")
    public ResponseEntity<?> deleteAlarm(HttpSession session,
                                         @PathVariable("contentName") String contentName) {
        String memberId = SessionUtils.getMemberId(session);

        if (memberId == null || memberId.isEmpty()) {
            throw new UnauthorizedException("로그인이 필요합니다.");
        }

        alarmService.deleteAlarm(memberId, contentName);
        return ResponseEntity.ok("알림 해제에 성공했습니다.");
    }
}
