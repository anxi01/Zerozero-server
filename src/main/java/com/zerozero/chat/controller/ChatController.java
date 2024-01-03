package com.zerozero.chat.controller;

import com.zerozero.chat.dto.request.ChatMessageRequest;
import com.zerozero.chat.dto.request.ChatRoomRequest;
import com.zerozero.chat.dto.response.ChatMessageResponse;
import com.zerozero.chat.dto.response.ChatRoomResponse;
import com.zerozero.chat.service.ChatMessageService;
import com.zerozero.chat.service.ChatRoomService;
import com.zerozero.user.entity.User;
import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {

  private final ChatRoomService chatRoomService;
  private final ChatMessageService chatMessageService;

  /* 채팅방 생성 */
  @PostMapping
  public ResponseEntity<ChatRoomResponse> createChatRoom(@RequestBody ChatRoomRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomService.createChatRoom(request));
  }

  /* 채팅방 전체 조회 */
  @GetMapping("/list")
  public ResponseEntity<List<ChatRoomResponse>> getAllChatRoom() {
    return ResponseEntity.ok(chatRoomService.getAllChatRoom());
  }

  @MessageMapping("/send-message")
  @SendTo("/topic/chat")
  public void sendMessage(ChatMessageRequest request) {
    Long userId = request.getUserId();
    Long roomId = request.getRoomId();
    String message = request.getMessage();

    chatRoomService.enterChatRoom(roomId, userId);
    chatMessageService.saveMessage(roomId, message, userId);
  }

  /* 채팅 내역 조회 */
  @GetMapping("/{roomId}")
  public ResponseEntity<List<ChatMessageResponse>> getChatMessage(@PathVariable Long roomId) {
    return ResponseEntity.ok(chatMessageService.getMessage(roomId));
  }

  /* 채팅방 나가기 */
  @PatchMapping("/exit")
  public ResponseEntity<String> closeChatRoom(Principal connectedUser) {
    chatRoomService.exitChatRoom(connectedUser);
    User user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
    return ResponseEntity.ok(user.getNickname() + "님이 퇴장하셨습니다.");
  }

  /* 웹소켓 통신을 위한 예시 */
  @GetMapping
  public String chatRoom(Model model) {
    return "chat"; // chat.html 템플릿을 찾아서 렌더링
  }
}
