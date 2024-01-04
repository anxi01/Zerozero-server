package com.zerozero.domain.chat.application;

import com.zerozero.domain.chat.domain.ChatRoom;
import com.zerozero.domain.chat.dto.request.ChatRoomRequest;
import com.zerozero.domain.chat.dto.response.ChatRoomResponse;
import com.zerozero.domain.chat.repository.ChatRoomRepository;
import com.zerozero.domain.user.domain.User;
import com.zerozero.domain.user.repository.UserRepository;
import java.security.Principal;
import java.util.List;
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

  public List<ChatRoomResponse> getAllChatRoom() {
    return chatRoomRepository.findAllByOrderByNameAsc().stream()
        .map(ChatRoomResponse::new).toList();
  }
}
