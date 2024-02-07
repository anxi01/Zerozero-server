package com.zerozero.domain.chat.application;

import com.zerozero.domain.chat.domain.ChatRoom;
import com.zerozero.domain.chat.dto.response.ChatRoomResponse;
import com.zerozero.domain.chat.repository.ChatRoomRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;

  @Transactional
  public void enterChatRoom(Long roomId, String username) {
    User user = userRepository.findByNickname(username)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

    user.enterChatRoom(chatRoom);
  }

  public List<ChatRoomResponse> getAllChatRoom() {
    return chatRoomRepository.findAllByOrderByNameAsc().stream()
        .map(ChatRoomResponse::new).toList();
  }
}
