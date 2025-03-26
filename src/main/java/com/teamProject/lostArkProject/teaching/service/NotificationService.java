package com.teamProject.lostArkProject.teaching.service;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationService {

    // mentorId -> SseEmitter
    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();

    /**
     * mentor가 SSE 연결을 맺을 때 호출됩니다.
     * - 타임아웃(Long.MAX_VALUE)을 사용하면 연결이 끊어지지 않고 유지됩니다.
     */
    public SseEmitter subscribe(String mentorId) {
        // 필요에 따라 타임아웃 시간 조정
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        // 혹시 기존에 열려있던 emitter가 있으면 제거 후 새로 등록(단일 연결 가정 시)
        emitterMap.put(mentorId, emitter);

        // 만약 멀티 연결을 허용하려면 Map<Long, List<SseEmitter>> 형태 등으로 구현

        // onCompletion / onTimeout 등 콜백 등록
        emitter.onCompletion(() -> emitterMap.remove(mentorId));
        emitter.onTimeout(() -> emitterMap.remove(mentorId));

        return emitter;
    }

    /**
     * 멘토에게 알림을 전송합니다.
     */
    public void sendNotification(String mentorId, String message) {
        SseEmitter emitter = emitterMap.get(mentorId);
        if (emitter != null) {
            try {
                // name("message")는 이벤트 이름
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data(message));
            } catch (IOException e) {
                // 에러 시 Map에서 제거
                emitterMap.remove(mentorId);
            }
        }
    }
}

