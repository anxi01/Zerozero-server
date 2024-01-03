package com.zerozero.chat.service;

import com.zerozero.chat.dto.response.ChatMessageResponse;
import com.zerozero.chat.entity.ChatMessage;
import com.zerozero.chat.entity.ChatRoom;
import com.zerozero.chat.repository.ChatMessageRepository;
import com.zerozero.chat.repository.ChatRoomRepository;
import com.zerozero.user.entity.User;
import com.zerozero.user.repository.UserRepository;
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
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
        () -> new IllegalArgumentException("해당 ChatRoom이 존재하지 않습니다. chatRoomId = " + chatRoomId));
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));
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
