package com.gdsc.projectmiobackend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.projectmiobackend.dto.ChatMessage;
import com.gdsc.projectmiobackend.dto.ChatRoom;
import com.gdsc.projectmiobackend.entity.ChatMessageEntity;
import com.gdsc.projectmiobackend.entity.ChatRoomEntity;
import com.gdsc.projectmiobackend.repository.ChatMessageRepository;
import com.gdsc.projectmiobackend.repository.ChatRoomRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {
    private final ObjectMapper objectMapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct
    private void init() {
        chatRooms = new ConcurrentHashMap<>(); // Use ConcurrentHashMap for thread safety
    }

    public ChatRoom findRoomById(String roomId) {
        ChatRoomEntity roomEntity = chatRoomRepository.findByRoomId(roomId);
        if (roomEntity != null) {
            return ChatRoom.builder()
                    .roomId(roomEntity.getRoomId())
                    .name(roomEntity.getName())
                    .build();
        }
        return null;
    }

    public List<ChatRoom> findAllRoom() {
        List<ChatRoomEntity> roomEntities = chatRoomRepository.findAll();
        List<ChatRoom> chatRooms = new ArrayList<>();
        for (ChatRoomEntity roomEntity : roomEntities) {
            ChatRoom chatRoom = ChatRoom.builder()
                    .roomId(roomEntity.getRoomId())
                    .name(roomEntity.getName())
                    .build();
            chatRooms.add(chatRoom);
        }
        return chatRooms;
    }
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();
        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);

        // ChatRoom을 ChatRoomEntity로 변환하여 데이터베이스에 저장
        ChatRoomEntity roomEntity = new ChatRoomEntity();
        roomEntity.setRoomId(chatRoom.getRoomId());
        roomEntity.setName(chatRoom.getName());
        chatRoomRepository.save(roomEntity);

        return chatRoom;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void saveMessage(ChatMessage chatMessage) {

        // 현재 인증된 사용자의 정보를 가져옴


        ChatMessageEntity messageEntity = new ChatMessageEntity();
        messageEntity.setRoomId(chatMessage.getRoomId());
        messageEntity.setSender(chatMessage.getSender());
        messageEntity.setMessage(chatMessage.getMessage());

        chatMessageRepository.save(messageEntity);
    }
}