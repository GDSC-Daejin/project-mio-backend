package com.gdsc.projectmiobackend.chat.controller;

import com.gdsc.projectmiobackend.chat.entity.ChatRoom;
import com.gdsc.projectmiobackend.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "채팅방")
public class ChatRoomController {
    private final ChatService chatService;

    /**
     * 채팅방 참여하기
     * @param roomId 채팅방 id
     */
//    @GetMapping("/{roomId}")
//    public String joinRoom(@PathVariable(required = false) Long roomId, Model model) {
//        List<Chat> chatList = chatService.findAllChatByRoomId(roomId);
//
//        model.addAttribute("roomId", roomId);
//        model.addAttribute("chatList", chatList);
//        return "room";
//    }

    /**
     * 채팅방 리스트 보기
     */
    @GetMapping("/roomList")
    @Operation(summary = "채팅리스트")
    public List<ChatRoom> roomList(Model model) {
        List<ChatRoom> roomList = chatService.findAllRoom();
        return roomList;
    }

    /**
     * 방만들기 폼
     */
    @PostMapping("/roomForm")
    @Operation(summary = "채팅방생성")
    public ChatRoom roomForm() {
        ChatRoom chatRoom = chatService.createRoom("방1");
        return chatRoom;
    }

}