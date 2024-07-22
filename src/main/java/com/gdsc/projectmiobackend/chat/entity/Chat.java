package com.gdsc.projectmiobackend.chat.entity;

import com.gdsc.projectmiobackend.chat.dto.ChatDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ChatRoom room;

    private String sender;

    private String message;

    private LocalDateTime sendTime;

    private ChatDto.MessageType type;

    public static Chat of(ChatDto dto, ChatRoom room){
        return Chat.builder()
                .room(room)
                .sender(dto.getSender())
                .message(dto.getMessage())
                .sendTime(LocalDateTime.now())
                .type(dto.getType())
                .build();
    }
}
