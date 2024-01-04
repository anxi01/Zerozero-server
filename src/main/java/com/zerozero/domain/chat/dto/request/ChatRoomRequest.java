package com.zerozero.domain.chat.dto.request;

import com.zerozero.domain.chat.domain.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomRequest {

  private String roomName;

  public ChatRoom toEntity() {
    return ChatRoom.builder()
        .roomName(this.roomName)
        .build();
  }
}
