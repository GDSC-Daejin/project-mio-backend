package com.gdsc.projectmiobackend.chat.repository;


import com.gdsc.projectmiobackend.chat.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
