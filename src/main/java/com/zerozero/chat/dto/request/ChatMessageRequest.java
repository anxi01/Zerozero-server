package com.zerozero.chat.dto.request;

import lombok.Data;

@Data
public class ChatMessageRequest {

  private Long userId;
  private String message;
  private Long roomId;
}
