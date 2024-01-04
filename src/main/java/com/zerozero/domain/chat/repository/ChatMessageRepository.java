package com.zerozero.domain.chat.repository;

import com.zerozero.domain.chat.domain.ChatMessage;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

  List<ChatMessage> findAllByChatRoom_Id(Long chatRoom_id);
}
