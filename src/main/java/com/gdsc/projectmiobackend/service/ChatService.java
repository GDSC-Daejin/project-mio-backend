package com.gdsc.projectmiobackend.service;

import com.gdsc.projectmiobackend.entity.ChatRoom;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;

public interface ChatService {

    List<ChatRoom> findAllRoom();

    ChatRoom findRoomById(String roomId);

    ChatRoom createRoom(String name);

    <T> void sendMessage(WebSocketSession session, T message);
}
