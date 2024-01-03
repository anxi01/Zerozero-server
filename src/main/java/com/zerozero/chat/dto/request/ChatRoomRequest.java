package com.zerozero.chat.dto.request;

import com.zerozero.chat.entity.ChatRoom;
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
