package com.gdsc.projectmiobackend.notification.controller;

import com.gdsc.projectmiobackend.notification.service.impl.NotificationServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationServiceImpl notificationService;

    @GetMapping(path = "/v1/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@RequestParam String userId) {
        SseEmitter emitter = notificationService.subscribe(userId);
        return ResponseEntity.ok(emitter);
    }

    @GetMapping(path = "/v1/subscribe/send")
    public ResponseEntity<SseEmitter> test(@RequestParam String userId) {
        notificationService.publish(userId);
        return ResponseEntity.ok().build();
    }
}