package com.teamProject.lostArkProject.teaching.service;

import com.teamProject.lostArkProject.teaching.dao.TeachingDAO;
import com.teamProject.lostArkProject.teaching.dto.MenteeApplyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    // =========================
    // 1) DAO 주입받기
    // =========================
    @Autowired
    private TeachingDAO teachingDAO;

    // SSE 연결을 멘토/멘티 각각 구분해서 관리
    private final Map<String, SseEmitter> mentorEmitters = new ConcurrentHashMap<>();
    private final Map<String, SseEmitter> menteeEmitters = new ConcurrentHashMap<>();

    // SSE 연결 유지 시간 (예: 30분)
    private static final long DEFAULT_TIMEOUT = 30 * 60 * 1000L;

    /**
     * 멘토가 SSE 구독(연결)할 때 호출됩니다.
     */
    public SseEmitter subscribeMentor(String mentorId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        mentorEmitters.put(mentorId, emitter);

        emitter.onCompletion(() -> mentorEmitters.remove(mentorId));
        emitter.onTimeout(() -> mentorEmitters.remove(mentorId));
        emitter.onError((e) -> mentorEmitters.remove(mentorId));

        try {
            emitter.send(SseEmitter.event().name("INIT").data("mentor connection established"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    /**
     * 멘티가 SSE 구독(연결)할 때 호출됩니다.
     */
    public SseEmitter subscribeMentee(String menteeId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        menteeEmitters.put(menteeId, emitter);

        emitter.onCompletion(() -> menteeEmitters.remove(menteeId));
        emitter.onTimeout(() -> menteeEmitters.remove(menteeId));
        emitter.onError((e) -> menteeEmitters.remove(menteeId));

        try {
            emitter.send(SseEmitter.event().name("INIT").data("mentee connection established"));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }
        return emitter;
    }

    // =====================
    // SSE 알림 전송 메서드
    // =====================
    /**
     * 멘토에게 알림을 전송합니다.
     */
    public void sendNotificationToMentor(String mentorId, String eventName, String data) {
        SseEmitter emitter = mentorEmitters.get(mentorId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
                mentorEmitters.remove(mentorId);
            }
        }
    }

    /**
     * 멘티에게 알림을 전송합니다.
     */
    public void sendNotificationToMentee(String menteeId, String eventName, String data) {
        SseEmitter emitter = menteeEmitters.get(menteeId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name(eventName).data(data));
            } catch (IOException e) {
                emitter.completeWithError(e);
                menteeEmitters.remove(menteeId);
            }
        }
    }

    // ======================
    // 2) 신청/수락/거절 로직
    // ======================

    /**
     * 멘티가 멘토에게 신청 (DB INSERT -> 'REQUESTED') 후, 멘토에게 SSE 알림
     */
    public void applyMentee(MenteeApplyDTO dto) {
        // 1) DB INSERT (REQUESTED)
        teachingDAO.insertMenteeApply(dto);

        // 2) SSE 알림: 멘토에게 "NEW_MENTEE_APPLY" 이벤트
        sendNotificationToMentor(
                dto.getMentor_member_id(),  // 멘토 ID
                "NEW_MENTEE_APPLY",         // 이벤트명
                dto.getMentee_member_id()   // data: 신청 보낸 멘티 ID
        );
    }

    /**
     * 멘토가 신청 수락 (DB UPDATE -> 'ACCEPTED') 후, 멘티에게 SSE로 멘토 디스코드 ID 전송
     */
    public void acceptMenteeApply(MenteeApplyDTO dto) {
        // 1) DB 업데이트
        teachingDAO.acceptMenteeApply(dto);

        // 2) 멘토 디스코드ID 조회
        String mentorDiscordId = teachingDAO.findDiscordIdByMentorId(dto.getMentor_member_id());

        // 3) SSE 알림: 멘티에게 "MENTEE_ACCEPTED"
        sendNotificationToMentee(
                dto.getMentee_member_id(),
                "MENTEE_ACCEPTED",
                mentorDiscordId
        );
    }

    /**
     * 멘토가 신청 거절 (DB UPDATE -> 'REJECTED') 후, 멘티에게 SSE 알림
     */
    public void rejectMenteeApply(MenteeApplyDTO dto) {
        // 1) DB 업데이트
        teachingDAO.rejectMenteeApply(dto);

        // 2) SSE 알림: "MENTEE_REJECTED"
        sendNotificationToMentee(
                dto.getMentee_member_id(),
                "MENTEE_REJECTED",
                "신청이 거절되었습니다."
        );
    }
}
