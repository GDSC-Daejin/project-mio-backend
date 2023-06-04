package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoomEntity, Long> {
    ChatRoomEntity findByRoomId(String roomId);
}