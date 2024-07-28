package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.AlarmDeleteDto;
import com.gdsc.projectmiobackend.dto.AlarmDto;
import com.gdsc.projectmiobackend.dto.request.AlarmCreateRequestDto;

import java.util.List;

public interface AlarmService {
    AlarmDto saveAlarm(AlarmCreateRequestDto alarm);

    List<AlarmDto> getAllAlarm(String email);

    AlarmDeleteDto deleteAlarm(Long id, String email);
}
