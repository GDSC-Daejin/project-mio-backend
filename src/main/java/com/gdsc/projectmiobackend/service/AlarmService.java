package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.dto.AlarmDto;
import com.gdsc.projectmiobackend.dto.request.AlarmCreateRequestDto;
import com.gdsc.projectmiobackend.entity.Alarm;

import java.util.List;

public interface AlarmService {
    AlarmDto saveAlarm(AlarmCreateRequestDto alarm);

    List<AlarmDto> getAllAlarm(String email);

    void deleteAlarm(Long id, String email);
}
