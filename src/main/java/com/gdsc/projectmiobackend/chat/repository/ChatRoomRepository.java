package com.gdsc.projectmiobackend.chat.repository;

import com.gdsc.projectmiobackend.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}