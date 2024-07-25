package com.gdsc.projectmiobackend.notification.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl {

    private final Map<String, SseEmitter> emitterMap = new ConcurrentHashMap<>();
    private static final long TIMEOUT = 6000 * 1000;
    private static final long RECONNECTION_TIMEOUT = 1000L;

    public SseEmitter subscribe(String userId) {

        SseEmitter emitter = new SseEmitter(TIMEOUT);
        emitter.onTimeout(() -> {
            log.info("timed out : {}", userId);
            emitter.complete();
        });

        //에러 핸들러 등록
        emitter.onError(e -> {
            log.info("Error message : {}", e.getMessage());
            emitter.complete();
        });

        //SSE complete 핸들러 등록
        emitter.onCompletion(() -> {
            if (emitterMap.remove(userId) != null) {
                log.info("Remove userId :{}", userId);
            }
            log.info("disconnect usrId : {}", userId);
        });

        emitterMap.put(userId, emitter);

        try {
            emitter.send(sseEventBuilder("subscribe",userId,"Subscribed successfully.")); //503 방지를위한 더미데이터
            emitter.send(sseEventBuilder("subscribe2",userId,"Subscribed successfully.")); //503 방지를위한 더미데이터
        } catch (IOException e) {
            log.error("IOException : , {}", e.getMessage());
        }
        return emitter;
    }

    public void publish(String userId) {
        SseEmitter emitter = emitterMap.get(userId);
        if(emitter != null) {
            try {
                emitter.send(sseEventBuilder("publish",userId,"Order status has been changed."));
                log.info("publish userId : {}", userId);
            } catch (IOException e) {
                log.error("IOException : {}", e.getMessage());
            }
        }
    }

    private SseEmitter.SseEventBuilder sseEventBuilder(String name, String userId, String message) {
        return SseEmitter.event()
                .name(name) //이벤트 명
                .id(userId) //이벤트 ID
                .data(message) //전송 데이터
                .reconnectTime(RECONNECTION_TIMEOUT); // 재연결 대기시작
    }
}