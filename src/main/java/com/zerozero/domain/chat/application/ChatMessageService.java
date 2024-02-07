package com.zerozero.domain.chat.application;

import com.zerozero.domain.chat.domain.ChatMessage;
import com.zerozero.domain.chat.domain.ChatRoom;
import com.zerozero.domain.chat.dto.request.ChatMessageRequest;
import com.zerozero.domain.chat.dto.response.ChatMessageResponse;
import com.zerozero.domain.chat.repository.ChatMessageRepository;
import com.zerozero.domain.chat.repository.ChatRoomRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

  private final ChatRoomRepository chatRoomRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final UserRepository userRepository;

  public ChatMessageResponse saveMessage(Long chatRoomId, ChatMessageRequest request) {
    ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(
        () -> new IllegalArgumentException("해당 ChatRoom이 존재하지 않습니다. chatRoomId = " + chatRoomId));

    User user = userRepository.findByNickname(request.getSender())
        .orElseThrow(() -> new IllegalArgumentException("해당 유저가 존재하지 않습니다."));

    ChatMessage chatMessage = ChatMessage.builder()
        .chatRoom(chatRoom)
        .message(request.getContent())
        .user(user)
        .build();

    chatMessageRepository.save(chatMessage);

    return new ChatMessageResponse(chatMessage);
  }

  public List<ChatMessageResponse> getMessage(Long roomId) {
    List<ChatMessage> chatMessages = chatMessageRepository.findAllByChatRoom_Id(roomId);
    return chatMessages.stream().map(ChatMessageResponse::new).toList();
  }
}
