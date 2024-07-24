package com.gdsc.projectmiobackend.notification.service.impl;

import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.notification.repository.EmitterRepository;
import com.gdsc.projectmiobackend.notification.service.NotificationService;
import com.gdsc.projectmiobackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final UserRepository userRepository;
    private final EmitterRepository emitterRepository;


    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;

    @Override
    public SseEmitter subscribe(String email) {
        UserEntity user = userRepository.findByEmail(email).orElseThrow();
        SseEmitter emitter = createEmitter(user.getId());

        sendToClient(user.getId(), "EventStream Created. [userId="+ user.getId() + "]", "sse 접속 성공");
        return emitter;
    }

    @Override
    public <T> void customNotify(Long userId, T data, String comment, String type) {
        sendToClient(userId, data, comment, type);
    }
    @Override
    public void notify(Long userId, Object data, String comment) {
        sendToClient(userId, data, comment);
    }

    private void sendToClient(Long userId, Object data, String comment) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name("sse")
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

    private <T> void sendToClient(Long userId, T data, String comment, String type) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name(type)
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }

    private UserEntity validUser(Long userId) {
        return userRepository.findById(userId).orElseThrow();
    }
}