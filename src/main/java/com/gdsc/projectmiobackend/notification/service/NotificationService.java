package com.gdsc.projectmiobackend.notification.service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface NotificationService {

    SseEmitter subscribe(String email);

    <T> void customNotify(Long userId, T data, String comment, String type);

    void notify(Long userId, Object data, String comment);
}