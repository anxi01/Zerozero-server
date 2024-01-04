package com.zerozero.domain.chat.dto.request;

import lombok.Data;

@Data
public class ChatMessageRequest {

  private Long userId;
  private String message;
  private Long roomId;
}
