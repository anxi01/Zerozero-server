package com.zerozero.domain.chat.api;

import com.zerozero.domain.chat.application.ChatMessageService;
import com.zerozero.domain.chat.application.ChatRoomService;
import com.zerozero.domain.chat.dto.request.ChatMessageRequest;
import com.zerozero.domain.chat.dto.response.ChatMessageResponse;
import com.zerozero.domain.chat.dto.response.ChatRoomResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ChatController {

  private final ChatRoomService chatRoomService;
  private final ChatMessageService chatMessageService;

  /* 채팅방 전체 조회 */
  @GetMapping("/chat/list")
  public ResponseEntity<List<ChatRoomResponse>> getAllChatRoom() {
    return ResponseEntity.ok(chatRoomService.getAllChatRoom());
  }

  /* 채팅방 입장 */
  @MessageMapping("/chat.addUser")
  @SendTo("/topic/{roomId}")
  public void addUser(@DestinationVariable Long roomId, @Payload ChatMessageRequest request,
      SimpMessageHeaderAccessor headerAccessor) {
    chatRoomService.enterChatRoom(roomId, request.getSender());
    headerAccessor.getSessionAttributes().put("username", request.getSender());
    headerAccessor.getSessionAttributes().put("roomId", roomId);
  }

  /* 메시지 전송 */
  @MessageMapping("/chat.sendMessage")
  @SendTo("/topic/{roomId}")
  public ChatMessageResponse sendMessage(@DestinationVariable Long roomId, @Payload ChatMessageRequest request) {
    return chatMessageService.saveMessage(roomId, request);
  }
}