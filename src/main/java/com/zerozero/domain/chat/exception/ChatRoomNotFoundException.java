package com.zerozero.domain.chat.exception;

import com.zerozero.global.error.ErrorCode;
import com.zerozero.global.error.exception.ServiceException;

public class ChatRoomNotFoundException extends ServiceException {

  public ChatRoomNotFoundException() {
    super(ErrorCode.CHAT_ROOM_NOT_FOUND_EXCEPTION);
  }
}
