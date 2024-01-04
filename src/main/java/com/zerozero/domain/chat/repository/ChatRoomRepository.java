package com.zerozero.domain.chat.repository;

import com.zerozero.domain.chat.domain.ChatRoom;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

  List<ChatRoom> findAllByOrderByNameAsc();
}
