package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.AlarmDeleteDto;
import com.gdsc.projectmiobackend.dto.AlarmDto;
import com.gdsc.projectmiobackend.dto.request.AlarmCreateRequestDto;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.AlarmService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "알람")
@RequestMapping(value = "/alarm", produces = MediaType.APPLICATION_JSON_VALUE)
public class AlarmController {

    private final AlarmService alarmService;

    @Operation(summary = "알람 생성")
    @PostMapping("/create")
    public ResponseEntity<AlarmDto> create(
            @RequestBody AlarmCreateRequestDto alarmCreateRequestDto){

        AlarmDto alarm = alarmService.saveAlarm(alarmCreateRequestDto);

        return ResponseEntity.ok(alarm);
    }

    @Operation(summary = "알람 조회")
    @GetMapping("/readAll")
    public ResponseEntity<List<AlarmDto>> readAll(
            @AuthenticationPrincipal UserInfo user){

        return ResponseEntity.ok(alarmService.getAllAlarm(user.getEmail()));
    }

    @Operation(summary = "알람 삭제")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<AlarmDeleteDto> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserInfo user){

        AlarmDeleteDto alarmDeleteDto = alarmService.deleteAlarm(id, user.getEmail());
        return ResponseEntity.ok(alarmDeleteDto);
    }
}
