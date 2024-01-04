package com.zerozero.domain.chat.dto.response;

import com.zerozero.domain.chat.domain.ChatMessage;
import java.time.format.DateTimeFormatter;
import lombok.Getter;

@Getter
public class ChatMessageResponse {

  private Long id;
  private String userName;
  private String message;
  private String createdAt;
  private String updatedAt;

  public ChatMessageResponse(ChatMessage chatMessage) {
    this.id = chatMessage.getId();
    this.userName = chatMessage.getUser().getNickname();
    this.message = chatMessage.getMessage();
    this.createdAt = chatMessage.getCreatedAt()
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
    this.updatedAt = chatMessage.getUpdatedAt()
        .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
  }
}
