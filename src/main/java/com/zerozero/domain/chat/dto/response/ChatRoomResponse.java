package com.zerozero.domain.chat.dto.response;

import com.zerozero.domain.chat.domain.ChatRoom;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class ChatRoomResponse {

  private Long id;
  private String roomName;
  private String createdAt;
  private String updatedAt;

  public ChatRoomResponse(ChatRoom chatRoom) {
    this.id = chatRoom.getId();
    this.roomName = chatRoom.getName();
    this.createdAt = chatRoom.getCreatedAt()
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    this.updatedAt = chatRoom.getUpdatedAt()
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
  }
}
