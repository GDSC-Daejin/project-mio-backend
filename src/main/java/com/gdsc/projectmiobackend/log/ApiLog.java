package com.gdsc.projectmiobackend.log;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String serverIp;

    @Column(length = 4096)
    private String requestUrl;

    private String requestMethod;

    private Integer responseStatus;

    private String clientIp;

    @Column(length = 4096)
    private String request;

    @Column(length = 4096)
    private String response;

    private LocalDateTime requestTime = LocalDateTime.now();

    private LocalDateTime responseTime;

    public ApiLog(String serverIp, String requestUrl, String requestMethod, String clientIp, String request) {
        this.serverIp = serverIp;
        this.requestUrl = requestUrl;
        this.requestMethod = requestMethod;
        this.clientIp = clientIp;
        this.request = request;
    }
}