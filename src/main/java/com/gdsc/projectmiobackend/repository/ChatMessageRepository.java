package com.gdsc.projectmiobackend.repository;

import com.gdsc.projectmiobackend.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long>{
}
