package com.zerozero.domain.chat.application;

import com.zerozero.domain.chat.domain.ChatMessage;
import com.zerozero.domain.chat.domain.ChatRoom;
import com.zerozero.domain.chat.dto.response.ChatMessageResponse;
import com.zerozero.domain.chat.exception.ChatRoomNotFoundException;
import com.zerozero.domain.chat.repository.ChatMessageRepository;
import com.zerozero.domain.chat.repository.ChatRoomRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.repository.UserRepository;
import com.zerozero.global.auth.exception.UserNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;

  public void saveMessage(Long chatRoomId, String message, Long userId) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(ChatRoomNotFoundException::new);
    User user = userRepository.findById(userId).orElseThrow(UserNotFoundException::new);

    ChatMessage chatMessage = ChatMessage.builder()
        .chatRoom(chatRoom)
        .message(message)
        .user(user)
        .build();
    chatMessageRepository.save(chatMessage);
  }

  public List<ChatMessageResponse> getMessage(Long roomId) {
    List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom_Id(roomId);
    return chatMessages.stream().map(ChatMessageResponse::new).toList();
  }
}
