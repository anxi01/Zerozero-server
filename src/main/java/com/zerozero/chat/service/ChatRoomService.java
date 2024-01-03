package com.zerozero.chat.service;

import com.zerozero.chat.dto.request.ChatRoomRequest;
import com.zerozero.chat.dto.response.ChatRoomResponse;
import com.zerozero.chat.entity.ChatRoom;
import com.zerozero.chat.repository.ChatRoomRepository;
import com.zerozero.user.entity.User;
import com.zerozero.user.repository.UserRepository;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

  private final ChatRoomRepository chatRoomRepository;
  private final UserRepository userRepository;

  @Transactional
  public void enterChatRoom(Long roomId, Long userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

    ChatRoom chatRoom = chatRoomRepository.findById(roomId)
        .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채팅방입니다."));

    user.enterChatRoom(chatRoom);
  }

  @Transactional
  public void exitChatRoom(Principal connectedUser) {

    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    user.exitChatRoom();
    userRepository.save(user);
  }

  /** ChatRoom 생성 */
  public ChatRoomResponse createChatRoom(ChatRoomRequest requestDto) {
    ChatRoom chatRoom = chatRoomRepository.save(requestDto.toEntity());
    return new ChatRoomResponse(chatRoom);
  }
}
