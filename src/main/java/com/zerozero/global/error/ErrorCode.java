package com.zerozero.global.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

  INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "서버 오류가 발생했습니다."),
  DUPLICATE_EMAIL_EXCEPTION(409, "DUPLICATE_EMAIL_EXCEPTION", "이메일이 중복됩니다."),
  DUPLICATE_NICKNAME_EXCEPTION(409, "DUPLICATE_NICKNAME_EXCEPTION", "닉네임이 중복됩니다."),
  USER_NOT_FOUND_EXCEPTION(404, "USER_NOT_FOUND_EXCEPTION", "사용자가 존재하지 않습니다."),
  STORE_NOT_FOUND_EXCEPTION(404, "STORE_NOT_FOUND_EXCEPTION", "판매점이 존재하지 않습니다.");

  private final int httpStatus;
  private final String code;
  private final String message;
}